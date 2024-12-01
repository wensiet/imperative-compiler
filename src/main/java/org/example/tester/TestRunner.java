package org.example.tester;

import compiler.CompileVisitor;
import gen.ImperativeCompConstLexer;
import gen.ImperativeCompConstParser;
import jasmin.JasMain;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import semantic.Optimizer;
import semantic.SemanticAnalyzerVisitor;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.System.exit;

public class TestRunner {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    private static final String filename = "Program";

    public void runUnsafe(TestCase testCase, Boolean useTestCase) throws IOException, InterruptedException {
        // Lexer
        ImperativeCompConstLexer lexer = new ImperativeCompConstLexer(CharStreams.fromString(testCase.input));

        // Parser
        ImperativeCompConstParser parser = new ImperativeCompConstParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new org.example.tester.ThrowingErrorStrategy());
        ImperativeCompConstParser.InputContext parseTree = parser.input();

        // Semantic
        SemanticAnalyzerVisitor semanticVisitor = new SemanticAnalyzerVisitor();
        semanticVisitor.visit(parseTree);

        // List<String> ruleNamesList = Arrays.asList(parser.getRuleNames());
        // String originalTree = TreeUtils.toPrettyTree(parseTree, ruleNamesList);

        // Optimizer
//        Optimizer optimizer = new Optimizer();
//        optimizer.optimize(parseTree);

        // Compiler
        List<String> files = new ArrayList<>();
        CompileVisitor compileVisitor = new CompileVisitor();
        compileVisitor.visit(parseTree);
        try (FileWriter fileWriter = new FileWriter(filename + ".j")) {
            fileWriter.write(compileVisitor.getJasminCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        files.add(filename + ".j");

        for (var record : compileVisitor.getRecords()) {
            String fileName = record.name + ".j";
            try (FileWriter fileWriter = new FileWriter(fileName)) {
                fileWriter.write(record.code);
            } catch (IOException e) {
                e.printStackTrace();
            }
            files.add(fileName);
        }
        JasMain jasMain = new JasMain();
        jasMain.run(files.toArray(new String[0]));
        buildProgramProcess(testCase.params, useTestCase);
    }

    public void run(TestCase testCase) throws InterruptedException, IOException {
        try {
            runUnsafe(testCase, true);
            System.out.println(ANSI_GREEN + "PASSED" + ANSI_RESET);
        } catch (Exception e) {
            Thread.sleep(500);
            System.out.println(ANSI_RED + "FAILED: " + ANSI_RESET + e.getMessage());
        }
    }

    private static void buildProgramProcess(RuntimeTestCaseParams params, Boolean useTestCase) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", filename);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder results = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            results.append(line).append("\n");
        }
        if (!results.isEmpty() && results.charAt(results.length() - 1) == '\n') {
            results.deleteCharAt(results.length() - 1);
        }
        System.out.println(results);
        if (useTestCase) {
            int exitCode = process.waitFor();
            if (exitCode != params.expectedExitCode) {
                throw new TestCaseException("Expected " + params.expectedExitCode + " exit code, but got: " + exitCode);
            }
            if (!Objects.equals(params.expectedOutput, results.toString())) {
                throw new TestCaseException("Output does not match. Expected:\n" + params.expectedOutput + "\n got:\n" + results);
            }
        }
    }
}
