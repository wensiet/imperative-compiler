package compiler;

import gen.ImperativeCompConstParser;

public class LoopsCompileVisitor extends IfCompileVisitor {
    @Override
    public Void visitFor_loop(ImperativeCompConstParser.For_loopContext ctx) {
        var varName = ctx.IDENT().getText();
        variableTable.put(varName, new Helper(stackIndex, "integer"));
        var variableHelper = variableTable.get(varName);
        if (variableHelper == null) {
            throw new RuntimeException("Variable not declared: " + varName);
        }
        int idx = variableHelper.idx;
        int endIndex = idx + 1;

        boolean isReverse = ctx.range().REVERSESYM() != null;

        if (isReverse) {
            visit(ctx.range().expression(1));
            appendln("istore " + idx);

            visit(ctx.range().expression(0));
            appendln("istore " + endIndex);
        } else {
            visit(ctx.range().expression(0));
            appendln("istore " + idx);

            visit(ctx.range().expression(1));
            appendln("istore " + endIndex);
        }

        String loopStartLabel = generateLabel();
        String endLoopLabel = generateLabel();

        appendln(loopStartLabel + ":");
        appendln("iload " + idx);
        appendln("iload " + endIndex);

        appendln(isReverse ? "if_icmplt " + endLoopLabel : "if_icmpgt " + endLoopLabel);

        visit(ctx.body());

        appendln(isReverse ? "iinc " + idx + " -1" : "iinc " + idx + " 1");

        appendln("goto " + loopStartLabel);

        appendln(endLoopLabel + ":");

        return null;
    }


    @Override
    public Void visitWhile_loop(ImperativeCompConstParser.While_loopContext ctx) {
        // Header
        var startLoopLabel = generateLabel();
        var endLoopLabel = generateLabel();
        appendln(startLoopLabel + ":");
        visit(ctx.expression());
        appendln("ifeq " + endLoopLabel);
        visit(ctx.body());

        // After body
        appendln("goto " + startLoopLabel);
        appendln(endLoopLabel + ":");
        return null;
    }
}
