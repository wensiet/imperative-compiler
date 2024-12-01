package compiler;

import gen.ImperativeCompConstParser;


public class RoutineCompileVisitor extends LoopsCompileVisitor {
    @Override
    public Void visitRoutine_declaration(ImperativeCompConstParser.Routine_declarationContext ctx) {
        RefactoredRoutineDeclaration routineVisitor = new RefactoredRoutineDeclaration(userDefinedTypes, labelCounter);
        routineVisitor.visit(ctx);
        routines.put(ctx.IDENT().getText(), routineVisitor.resultingRoutine);
        labelCounter = routineVisitor.labelCounter;
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
            var printArgText = printArg.getText().replaceAll("\\[\\d+\\]", "");
            var varTypeAssociation = "I";
            if (variableTable.containsKey(printArgText)) {
                var varType = variableTable.get(printArgText).type;
                if (varType.contains("real")) {
                    varTypeAssociation = "F";
                }
            }


            if (arguments.routine_call_arguments() != null) {
                throw new IllegalStateException("Print accepts exactly one argument");
            }

            visit(printArg);

            appendln(String.format("invokevirtual java/io/PrintStream/println(%s)V", varTypeAssociation));
            return null;
        }

        var routine = routines.get(routineName);
        if (routine == null) {
            throw new IllegalStateException("No such routine " + routineName);
        }
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
