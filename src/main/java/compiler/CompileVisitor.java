package compiler;

import gen.ImperativeCompConstBaseVisitor;
import gen.ImperativeCompConstParser;

import java.util.HashMap;
import java.util.Map;

public class CompileVisitor extends ImperativeCompConstBaseVisitor<Void> {
    Map<String, Integer> variableTable = new HashMap<>();
    Integer stackIndex = 1;
    StringBuilder jasminCode = new StringBuilder();

    @Override
    public Void visitProgram(ImperativeCompConstParser.ProgramContext ctx) {
        System.out.println(".class public Program");
        System.out.println(".super java/lang/Object");

        System.out.println(".method public static main([Ljava/lang/String;)V");

        super.visitProgram(ctx);

        System.out.println(".end method");

        return null;
    }

    @Override
    public Void visitPrimary(ImperativeCompConstParser.PrimaryContext ctx) {
        if (ctx.INT() != null) {
            String intValue = ctx.INT().getText();
            System.out.println("ldc " + intValue);
        } else if (ctx.REAL() != null) {
            String realValue = ctx.REAL().getText();
            System.out.println("ldc " + realValue);
        } else if (ctx.TRUE() != null) {
            System.out.println("iconst_1");
        } else if (ctx.FALSE() != null) {
            System.out.println("iconst_0");
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
        System.out.println("istore " + idx);
        return null;
    }

    @Override
    public Void visitModifiable_primary(ImperativeCompConstParser.Modifiable_primaryContext ctx) {
        if (ctx.IDENT() != null) {
            String variableName = ctx.IDENT().getText();
            System.out.println("aload " + variableTable.get(variableName));
        } else if (ctx.LBRACKET() != null && ctx.RBRACKET() != null) {
            visit(ctx.modifiable_primary());
            System.out.println("iaload");
        } else if (ctx.PERIOD() != null) {
            visit(ctx.modifiable_primary());
            String fieldName = ctx.IDENT().getText();
            System.out.println("getfield TODO(" + fieldName + ")");
        }
        return null;
    }

    @Override
    public Void visitRoutine_call(ImperativeCompConstParser.Routine_callContext ctx) {
        System.out.println("getstatic java/lang/System/out Ljava/io/PrintStream;");

        if (ctx.routine_call_arguments() != null) {
            visit(ctx.routine_call_arguments());
        }

        System.out.println("invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");

        return null;
    }

}
