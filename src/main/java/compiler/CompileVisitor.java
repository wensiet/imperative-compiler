package compiler;

import gen.ImperativeCompConstBaseVisitor;
import gen.ImperativeCompConstParser;

import java.util.HashMap;
import java.util.Map;

public class CompileVisitor extends ImperativeCompConstBaseVisitor<Void> {
    Map<String, Integer> variableTable = new HashMap<>();
    Integer stackIndex = 1;
    private final StringBuilder jasminCode = new StringBuilder();

    public String getJasminCode() {
        return jasminCode.toString();
    }

    private void appendNewLine(String line) {
        jasminCode.append(line).append("\n");
    }

    @Override
    public Void visitProgram(ImperativeCompConstParser.ProgramContext ctx) {
        appendNewLine(".class public Program");
        appendNewLine(".super java/lang/Object");

        appendNewLine(".method public static main([Ljava/lang/String;)V");

        super.visitProgram(ctx);

        appendNewLine("return");
        appendNewLine(".end method");

        return null;
    }

    @Override
    public Void visitPrimary(ImperativeCompConstParser.PrimaryContext ctx) {
        if (ctx.INT() != null) {
            String intValue = ctx.INT().getText();
            appendNewLine("ldc " + intValue);
        } else if (ctx.REAL() != null) {
            String realValue = ctx.REAL().getText();
            appendNewLine("ldc " + realValue);
        } else if (ctx.TRUE() != null) {
            appendNewLine("iconst_1");
        } else if (ctx.FALSE() != null) {
            appendNewLine("iconst_0");
        } else if (ctx.modifiable_primary() != null) {
            visit(ctx.modifiable_primary());
        }
        return null;
    }

    @Override
    public Void visitType(ImperativeCompConstParser.TypeContext ctx) {
        return super.visitType(ctx);
    }

    @Override
    public Void visitVariable_declaration(ImperativeCompConstParser.Variable_declarationContext ctx) {
        String varName = ctx.IDENT().getText();
        Integer idx = stackIndex++;
        variableTable.put(varName, idx);
        super.visitVariable_declaration(ctx);
        appendNewLine("istore " + idx);
        return null;
    }

    @Override
    public Void visitModifiable_primary(ImperativeCompConstParser.Modifiable_primaryContext ctx) {
        if (ctx.IDENT() != null) {
            String variableName = ctx.IDENT().getText();
            appendNewLine("iload " + variableTable.get(variableName));
        } else if (ctx.LBRACKET() != null && ctx.RBRACKET() != null) {
            visit(ctx.modifiable_primary());
            appendNewLine("iload");
        } else if (ctx.PERIOD() != null) {
            visit(ctx.modifiable_primary());
            String fieldName = ctx.IDENT().getText();
            appendNewLine("getfield TODO(" + fieldName + ")");
        }
        return null;
    }

    @Override
    public Void visitRoutine_call(ImperativeCompConstParser.Routine_callContext ctx) {
        if (ctx.IDENT().getText().equals("print")) {
            appendNewLine("getstatic java/lang/System/out Ljava/io/PrintStream;");

            if (ctx.routine_call_arguments() != null) {
                visit(ctx.routine_call_arguments());
            }

            appendNewLine("invokevirtual java/io/PrintStream/println(I)V");
        }

        return null;
    }
}
