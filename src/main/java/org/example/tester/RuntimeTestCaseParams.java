package org.example.tester;

public class RuntimeTestCaseParams {
    public int expectedExitCode;
    public String expectedOutput;

    public RuntimeTestCaseParams(int expectedExitCode, String expectedOutput) {
        this.expectedExitCode = expectedExitCode;
        this.expectedOutput = expectedOutput;
    }
}
