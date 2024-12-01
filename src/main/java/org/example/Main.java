package org.example;

import org.example.tester.RuntimeTestCaseParams;
import org.example.tester.TestCase;
import org.example.tester.TestLoader;
import org.example.tester.TestRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.System.exit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private boolean singleTest = true;

    private static String readFile(File file) {
        try {
            return Files.readString(Paths.get(file.getPath()));
        } catch (IOException e) {
            System.out.println(e);
            exit(1);
        }
        return "";
    }


    private static void runSingleTest() throws InterruptedException, IOException {
        String input = readFile(new File("program"));
        TestRunner runner = new TestRunner();
        runner.runUnsafe(new TestCase("program", input, new RuntimeTestCaseParams(0, "1")), false);
    }

    public static void main(String[] args) throws InterruptedException {

        try {
//            TestRunner runner = new TestRunner();
//            List<TestCase> tests = new TestLoader("conftest.yaml").getTestCases();
//            for (TestCase test : tests) {
//                System.out.println("Running test case: " + test.name);
//                runner.run(test);
//            }
            runSingleTest();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}