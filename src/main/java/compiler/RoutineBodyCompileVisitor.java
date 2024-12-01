package compiler;

import gen.ImperativeCompConstParser;

public class RoutineBodyCompileVisitor extends CompileVisitor {
    @Override
    public Void visitReturn_expression(ImperativeCompConstParser.Return_expressionContext ctx) {
        appendln("ireturn");
        return null;
    }
}
