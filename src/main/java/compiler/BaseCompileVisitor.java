package compiler;

import gen.ImperativeCompConstBaseVisitor;
import gen.ImperativeCompConstParser;
import org.antlr.v4.runtime.ParserRuleContext;

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

    protected void append(String text) {
        jasminCode.append(text);
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

    protected String getExpressionType(ParserRuleContext ctx) {
        switch (ctx) {
            case ImperativeCompConstParser.PrimaryContext primaryCtx -> {
                if (primaryCtx.INT() != null) return "integer";
                if (primaryCtx.REAL() != null) return "real";
                if (primaryCtx.TRUE() != null || primaryCtx.FALSE() != null) return "boolean";
                if (primaryCtx.modifiable_primary() != null) {
                    String variableName = primaryCtx.modifiable_primary().IDENT().getText();
                    Helper variableHelper = variableTable.get(variableName);
                    if (variableHelper == null) {
                        throw new IllegalStateException("Variable '" + variableName + "' is not defined");
                    }
                    return variableHelper.type;
                }
            }
            case ImperativeCompConstParser.Modifiable_primaryContext modifiableCtx -> {
                String variableName = modifiableCtx.IDENT().getText();
                Helper variableHelper = variableTable.get(variableName);
                if (variableHelper == null) {
                    throw new IllegalStateException("Variable '" + variableName + "' is not defined");
                }
                return variableHelper.type;
            }
            case ImperativeCompConstParser.SummandContext summandContext -> {
                return getExpressionType(summandContext.primary());
            }
            case ImperativeCompConstParser.FactorContext factorContext -> {
                return getExpressionType(factorContext.summand(0));
            }
            default -> {
            }
        }
        throw new IllegalStateException("Unable to determine type for expression");
    }
}
