package org.example.tester;

public class TestCase {
    public String name;
    public String input;
    public RuntimeTestCaseParams params;

    public TestCase(String name, String input, RuntimeTestCaseParams params) {
        this.name = name;
        this.input = input;
        this.params = params;
    }

}