package compiler;

import gen.ImperativeCompConstBaseVisitor;

import java.util.HashMap;
import java.util.Map;

public class BaseCompileVisitor extends ImperativeCompConstBaseVisitor<Void> {
    Map<String, Helper> variableTable = new HashMap<>();
    Integer stackIndex = 1;
    private final StringBuilder jasminCode = new StringBuilder();
    private int labelCounter = 0;

    public String getJasminCode() {
        return jasminCode.toString();
    }

    protected void appendln(String line) {
        jasminCode.append(line).append("\n");
    }

    protected String generateLabel() {
        return "LABEL_" + (labelCounter++);
    }
}
