package compiler;

import gen.ImperativeCompConstParser;

public class BodyCompileVisitor extends RoutineCompileVisitor {
    @Override
    public Void visitAssignment(ImperativeCompConstParser.AssignmentContext ctx) {
        visit(ctx.expression() != null ? ctx.expression() : ctx.routine_call());

        Helper variableInfo = variableTable.get(ctx.modifiable_primary().getText());
        if (variableInfo == null) {
            throw new RuntimeException("Variable not declared: " + ctx.modifiable_primary().getText());
        }

        switch (variableInfo.type) {
            case "integer", "boolean":
                appendln("istore " + variableInfo.idx);
                break;
            case "real":
                appendln("fstore " + variableInfo.idx);
                break;
            default:
                appendln("astore " + variableInfo.idx);
                break;
        }

        return null;
    }

}
