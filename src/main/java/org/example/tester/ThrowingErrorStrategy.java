package org.example.tester;

import org.antlr.v4.runtime.*;

public class ThrowingErrorStrategy extends DefaultErrorStrategy {

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
