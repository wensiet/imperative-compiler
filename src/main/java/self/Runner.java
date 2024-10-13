package self;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Runner {
    static void run() {
        runTestCases();
    }


    private static void processInput(String input) {
        Lexer lexer = new Lexer(input);
        Token token = lexer.nextToken();
        while (token != null) {
            System.out.println(token);
            token = lexer.nextToken();
        }
    }

    public static String readFile(File file) {
        try {
            return Files.readString(Paths.get(file.getPath()));
        } catch (IOException e) {
            System.out.println("Error reading file: " + file.getName());
            return "";
        }
    }

    public static void runTestCases() {
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
                String testInput = readFile(testCase);
                System.out.println("Input:\n" + testInput);
                processInput(testInput);
            }
        } else {
            System.out.println("No test cases found in directory: " + testCasesFileDirectory);
        }
    }
}
