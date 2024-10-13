package org.example;

import gen.ImperativeCompConstLexer;
import gen.ImperativeCompConstParser;
import org.antlr.v4.runtime.*;
import self.Runner;

import java.io.File;
import java.util.Arrays;

class ThrowingErrorStrategy extends DefaultErrorStrategy {

    @Override
    public void recover(Parser recognizer, RecognitionException e) {
        throw new RuntimeException(e);
    }

    @Override
    public Token recoverInline(Parser recognizer) throws RecognitionException {
        InputMismatchException e = new InputMismatchException(recognizer);
        throw new RuntimeException(e);
    }

    @Override
    public void sync(Parser recognizer) throws RecognitionException {
    }
}

public class GenRunner {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";


    private static void processInput(String input) throws InterruptedException {
        try {
            ImperativeCompConstLexer lexer = new ImperativeCompConstLexer(CharStreams.fromString(input));
            ImperativeCompConstParser parser = new ImperativeCompConstParser(new CommonTokenStream(lexer));
            parser.setErrorHandler(new ThrowingErrorStrategy());
            parser.input();
            System.out.println(ANSI_GREEN + "PASSED" + ANSI_RESET);
        } catch (Exception e) {
            Thread.sleep(500);
            System.out.println(ANSI_RED + "FAILED: " + ANSI_RESET + e.getMessage());
        }
    }

    public static void runTestCases() throws InterruptedException {
        String testCasesFileDirectory = "test_cases";
        File testCasesDirectory = new File(testCasesFileDirectory);
        File[] testCases = testCasesDirectory.listFiles();
        if (testCases != null) {
            Arrays.sort(testCases, (file1, file2) -> {
                int num1 = Integer.parseInt(file1.getName());
                int num2 = Integer.parseInt(file2.getName());
                return Integer.compare(num1, num2);
            });
            for (File testCase : testCases) {
                System.out.println("\nRunning test case: " + testCase.getName());
                String testInput = Runner.readFile(testCase);
                System.out.println("Input:\n" + testInput);
                processInput(testInput);
            }
        } else {
            System.out.println("No test cases found in directory: " + testCasesFileDirectory);
        }
    }
}