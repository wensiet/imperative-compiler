package compiler;

import gen.ImperativeCompConstParser;

import java.util.ArrayList;
import java.util.List;

public class RoutineCompileVisitor extends LoopsCompileVisitor {
    Routine curRoutine;
    @Override
    public Void visitRoutine_declaration(ImperativeCompConstParser.Routine_declarationContext ctx) {
        RoutineDeclarationCompileVisitor routineVisitor = new RoutineDeclarationCompileVisitor();
        routineVisitor.routines = routines;
        routineVisitor.visit(ctx);
        routines.put(routineVisitor.routine.name, routineVisitor.routine);
        return null;
    }

    @Override
    public Void visitRoutine_call(ImperativeCompConstParser.Routine_callContext ctx) {
        String routineName = ctx.IDENT().getText();
        var arguments = ctx.routine_call_arguments();

        if (routineName.equals("print")) {
            if (arguments == null) return null;

            appendln("getstatic java/lang/System/out Ljava/io/PrintStream;");

            var printArg = arguments.expression();

            if (arguments.routine_call_arguments() != null) {
                throw new IllegalStateException("Print accepts exactly one argument");
            }

            visit(printArg);

            appendln("invokevirtual java/io/PrintStream/println(I)V");
            return null;
        }

        var routine = routines.get(routineName);
        if (routine == null) {
            throw new IllegalStateException("No such routine " + routineName);
        }
        curRoutine = routine;
        if (arguments != null) visit(arguments);

        appendln("invokestatic Program/" + routine.signature);
        return null;
    }

    @Override
    public Void visitRoutine_call_arguments(ImperativeCompConstParser.Routine_call_argumentsContext ctx) {
        if (ctx.routine_call_arguments() != null) visit(ctx.routine_call_arguments());
        return visit(ctx.expression());
    }
}
