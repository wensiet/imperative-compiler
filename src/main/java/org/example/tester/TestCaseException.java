package org.example.tester;

import java.io.IOException;

public class TestCaseException extends IOException {
    public TestCaseException(String message) {
        super(message);
    }
}