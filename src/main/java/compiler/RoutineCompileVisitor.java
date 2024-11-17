package compiler;

import gen.ImperativeCompConstParser;

public class RoutineCompileVisitor extends LoopsCompileVisitor {
    @Override
    public Void visitRoutine_call(ImperativeCompConstParser.Routine_callContext ctx) {
        if (ctx.IDENT().getText().equals("print")) {
            appendln("getstatic java/lang/System/out Ljava/io/PrintStream;");

            if (ctx.routine_call_arguments() != null) {
                visit(ctx.routine_call_arguments());
            }

            appendln("invokevirtual java/io/PrintStream/println(I)V");
        }

        return null;
    }
}
