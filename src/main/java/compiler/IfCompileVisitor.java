package compiler;

import gen.ImperativeCompConstParser;

public class IfCompileVisitor extends ExpressionCompileVisitor {
    @Override
    public Void visitIf_statement(ImperativeCompConstParser.If_statementContext ctx) {
        visit(ctx.expression());

        String elseLabel = generateLabel();
        String endLabel = generateLabel();

        appendln("ifeq " + elseLabel);
        visit(ctx.body(0));
        appendln("goto " + endLabel);
        appendln(elseLabel + ":");

        if (ctx.ELSESYM() != null) {
            visit(ctx.body(1));
        }

        appendln(endLabel + ":");

        return null;
    }

}
