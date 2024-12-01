package compiler;

import gen.ImperativeCompConstBaseVisitor;

import java.util.*;

public class BaseCompileVisitor extends ImperativeCompConstBaseVisitor<Void> {
    Map<String, Helper> variableTable = new HashMap<>();
    Map<String, UserDefinedType> userDefinedTypes = new HashMap<>();
    Integer stackIndex = 1;
    private final StringBuilder jasminCode = new StringBuilder();
    protected final HashMap<String, Routine> routines = new HashMap<>();
    private final ArrayList<Record> records = new ArrayList<>();
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

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void addRecord(Record record) {
        records.add(record);
    }

    protected String mapType(String type) {
        return switch (type) {
            case "integer" -> "I";
            case "real" -> "F";
            case "boolean" -> "Z";
            default -> "L" + type + ";"; // User-defined or complex types
        };
    }
}
