package compiler;

import gen.ImperativeCompConstParser;

import java.util.ArrayList;

public class RoutineCompileVisitor extends LoopsCompileVisitor {
    public Routine currentRoutine = null;

    @Override
    public Void visitRoutine_call(ImperativeCompConstParser.Routine_callContext ctx) {
        String currentRoutineName = ctx.IDENT().getText();
        var arguments = ctx.routine_call_arguments();
        if (currentRoutineName.equals("print")) {
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
                throw new IllegalStateException();
            }

            visit(printArg);

            appendln(String.format("invokevirtual java/io/PrintStream/println(%s)V", varTypeAssociation));
        }
        return null;
    }

    @Override
    public Void visitRoutine_declaration(ImperativeCompConstParser.Routine_declarationContext ctx) {
        Routine routine = new Routine();
        String routineName = ctx.IDENT().getText();
        routine.name = routineName;

        var functionBody = new StringBuilder(".method public static ");
        functionBody.append(routineName);

        if (routines.get(routineName) != null) {
            throw new IllegalStateException();
        }

        StringBuilder methodDescriptor = new StringBuilder("(");
        var params = ctx.parameter_declarations();
        if (params != null) {
            visitParameter_declarations(ctx.parameter_declarations());
            ArrayList<String> paramList = new ArrayList<>();

            while (params != null) {
                paramList.add(mapType(params.type().getText()));
                params = params.parameter_declarations();
            }
            for (String p : paramList.reversed()) {
                methodDescriptor.append(p);
            }
        }

        methodDescriptor.append(")");

        if (ctx.type() != null) {
            methodDescriptor.append(mapType(ctx.type().getText()));
        } else {
            methodDescriptor.append("V");
        }

        functionBody.append(methodDescriptor).append("\n");
        functionBody.append(".limit stack 1000").append("\n");
        functionBody.append(".limit locals 1000").append("\n");

        RoutineBodyCompileVisitor visitor = new RoutineBodyCompileVisitor();
        visitor.visit(ctx.body());
        functionBody.append(visitor.getJasminCode());

        functionBody.append(".end method");

        routine.body = functionBody.toString();

        routines.put(routineName, routine);

        return null;
    }
}

//
//---
//import gen.ImperativeCompConstParser;
//
//public class RoutineCompileVisitor extends LoopsCompileVisitor {
//    private StringBuilder jasminCode;
//    private int localVarIndex;
//    private String currentRoutineName;
//    private String returnType;
//
//    public RoutineCompileVisitor() {
//        jasminCode = new StringBuilder();
//        localVarIndex = 0;
//    }
//
//    @Override
//    public Void visitRoutine_declaration(ImperativeCompConstParser.Routine_declarationContext ctx) {
//        // Get routine name
//        currentRoutineName = ctx.IDENT().getText();
//
//        // Start method declaration
//        jasminCode.append(".method public static ").append(currentRoutineName);
//
//        // Handle parameters
//        StringBuilder methodDescriptor = new StringBuilder("(");
//        if (ctx.parameter_declarations() != null) {
//            visitParameter_declarations(ctx.parameter_declarations());
//            // Add parameter types to method descriptor
//            for (ImperativeCompConstParser.Parameter_declarationsContext param :
//                    ctx.parameter_declarations().parameter_declarations()) {
//                methodDescriptor.append(getJasminType(param.type().getText()));
//            }
//        }
//
//        // Handle return type
//        if (ctx.type() != null) {
//            returnType = getJasminType(ctx.type().getText());
//        } else {
//            returnType = "V"; // void return type
//        }
//        methodDescriptor.append(")").append(returnType);
//
//        jasminCode.append(methodDescriptor.toString()).append("\n");
//        jasminCode.append(".limit stack 100\n"); // You might want to calculate this
//        jasminCode.append(".limit locals 100\n"); // You might want to calculate this
//
//        // Visit method body
//        if (ctx.body() != null) {
//            visit(ctx.body());
//        }
//
//        // Add return statement if needed
//        if (returnType.equals("V")) {
//            jasminCode.append("    return\n");
//        }
//
//        jasminCode.append(".end method\n\n");
//
//        return null;
//    }
//
//    @Override
//    public Void visitRoutine_call(ImperativeCompConstParser.Routine_callContext ctx) {
//        String routineName = ctx.IDENT().getText();
//
//        // Push arguments onto the stack
//        if (ctx.routine_call_arguments() != null) {
//            visit(ctx.routine_call_arguments());
//        }
//
//        // Generate invokestatic instruction
//        jasminCode.append("    invokestatic ").append(getClassName())
//                .append("/").append(routineName);
//
//        // Add method descriptor (you'll need to look up the actual method signature)
//        // This is a simplified example
//        jasminCode.append("()V\n");
//
//        return null;
//    }
//
//    private String getJasminType(String sourceType) {
//        switch (sourceType.toLowerCase()) {
//            case "int":
//                return "I";
//            case "real":
//                return "F";
//            case "bool":
//                return "Z";
//            default:
//                return "L" + sourceType + ";";
//        }
//    }
//
//    private String getClassName() {
//        // Return the name of your class
//        return "YourClassName";
//    }
//
//    @Override
//    public Void visitReturn_expression(ImperativeCompConstParser.Return_expressionContext ctx) {
//        // Visit the expression to push the return value onto the stack
//        visit(ctx.expression());
//
//        // Add appropriate return instruction based on return type
//        switch (returnType) {
//            case "I":
//                jasminCode.append("    ireturn\n");
//                break;
//            case "F":
//                jasminCode.append("    freturn\n");
//                break;
//            case "Z":
//
//
//                jasminCode.append("    ireturn\n");
//                break;
//            case "V":
//                jasminCode.append("    return\n");
//                break;
//            default:
//                jasminCode.append("    areturn\n");
//        }
//
//        return null;
//    }
//
//    public String getJasminCode() {
//        return jasminCode.toString();
//    }
//}
