package compiler;

import gen.ImperativeCompConstBaseVisitor;
import gen.ImperativeCompConstParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutineDeclarationCompileVisitor extends ImperativeCompConstBaseVisitor<Void> {
    public Routine routine;
    boolean inRoutine = false;
    Map<String, Helper> variableTable = new HashMap<>();
    private int labelCounter = 0;
    public Map<String, UserDefinedType> userDefinedTypes = new HashMap<>();
    int localsIndex = 0;
    protected HashMap<String, Routine> routines;
    Routine curRoutine;


    private int addLocal(String name, String type) {
        variableTable.put(name, new Helper(localsIndex, type));
        return localsIndex++;
    }

    protected String generateLabel() {
        return "LABEL_" + (labelCounter++);
    }

    protected String mapType(String type) {
        return switch (type) {
            case "integer" -> "I";
            case "real" -> "F";
            case "boolean" -> "Z";
            default -> "L" + type + ";";
        };
    }

    @Override
    public Void visitRoutine_declaration(ImperativeCompConstParser.Routine_declarationContext ctx) {
        if (inRoutine) {
            throw new IllegalStateException("Cannot declare nested routine");
        }
        inRoutine = true;
        String routineName = ctx.IDENT().getText();
        String returnType;

        if (ctx.type() != null) {
            returnType = mapType(ctx.type().getText());
        } else {
            returnType = "V";
        }

        routine = new Routine(routineName, returnType);

        routine.body.append(".method public static ");

        StringBuilder signature = new StringBuilder(routineName);

        signature.append("(");
        visit(ctx.parameter_declarations());
        routine.args = routine.args.reversed();
        for (Argument arg : routine.args) {
            signature.append(arg.type);
        }
        signature.append(")");

        signature.append(returnType);
        routine.signature = signature.toString();

        routine.body.append(signature).append("\n");
        routine.body.append(".limit stack 10").append("\n");
        routine.body.append(".limit locals 10").append("\n");

        visit(ctx.body());

        if (returnType.equals("V")) routine.body.append("return\n");
        routine.body.append(".end method");

        return null;
    }


    @Override
    public Void visitRoutine_call(ImperativeCompConstParser.Routine_callContext ctx) {
        String routineName = ctx.IDENT().getText();
        var arguments = ctx.routine_call_arguments();

        if (routineName.equals("print")) {
            if (arguments == null) return null;

            routine.body.append("getstatic java/lang/System/out Ljava/io/PrintStream;").append("\n");

            var printArg = arguments.expression();

            if (arguments.routine_call_arguments() != null) {
                throw new IllegalStateException("Print accepts exactly one argument");
            }

            visit(printArg);

            routine.body.append("invokevirtual java/io/PrintStream/println(I)V").append("\n");
            return null;
        }

        var callee = routines.get(routineName);
        if (callee == null) {
            throw new IllegalStateException("No such routine " + routineName);
        }
        curRoutine = callee;
        if (arguments != null) visit(arguments);

        routine.body.append("invokestatic Program/" + callee.signature).append("\n");
        return null;
    }

    @Override
    public Void visitRoutine_call_arguments(ImperativeCompConstParser.Routine_call_argumentsContext ctx) {
        if (ctx.routine_call_arguments() != null) visit(ctx.routine_call_arguments());
        return visit(ctx.expression());
    }

    @Override
    public Void visitParameter_declarations(ImperativeCompConstParser.Parameter_declarationsContext ctx) {
        if (ctx.parameter_declarations() != null) visit(ctx.parameter_declarations());
        String name = ctx.IDENT().getText();
        String type = mapType(ctx.type().getText());
        var arg = new Argument(ctx.IDENT().getText(), type);
        addLocal(name, type);
        routine.args.add(arg);
        return null;
    }

    @Override
    public Void visitExpression(ImperativeCompConstParser.ExpressionContext ctx) {
        if (ctx.relation() != null && ctx.relation().size() == 1) {
            return visit(ctx.relation(0));
        }

        if (ctx.routine_call() != null) {
            return visit(ctx.routine_call());
        }

        if (ctx.AND() != null) {
            visit(ctx.relation(0));
            visit(ctx.relation(1));
            routine.body.append("iand\n").append("\n");
        } else if (ctx.OR() != null) {
            visit(ctx.relation(0));
            visit(ctx.relation(1));
            routine.body.append("ior").append("\n");
        } else if (ctx.XOR() != null) {
            visit(ctx.relation(0));
            visit(ctx.relation(1));
            routine.body.append("ixor").append("\n");
        }
        return null;
    }

    @Override
    public Void visitRelation(ImperativeCompConstParser.RelationContext ctx) {
        if (ctx.simple(0) != null && ctx.simple(1) == null) {
            return visit(ctx.simple(0));
        }
        visit(ctx.simple(0));

        visit(ctx.simple(1));

        String labelTrue = generateLabel();
        if (ctx.LSS() != null) {
            routine.body.append("if_icmplt " + labelTrue).append("\n");
        } else if (ctx.LEQ() != null) {
            routine.body.append("if_icmple " + labelTrue).append("\n");
        } else if (ctx.GTR() != null) {
            routine.body.append("if_icmpgt " + labelTrue).append("\n");
        } else if (ctx.GEQ() != null) {
            routine.body.append("if_icmpge " + labelTrue).append("\n");
        } else if (ctx.EQL() != null) {
            routine.body.append("if_icmpeq " + labelTrue).append("\n");
        } else if (ctx.NEQ() != null) {
            routine.body.append("if_icmpne " + labelTrue).append("\n");
        }

        String labelEnd = generateLabel();

        routine.body.append("iconst_0").append("\n");
        routine.body.append("goto " + labelEnd).append("\n");

        routine.body.append(labelTrue + ":").append("\n");
        routine.body.append("iconst_1").append("\n");

        routine.body.append(labelEnd + ":").append("\n");
        return null;
    }

    @Override
    public Void visitSimple(ImperativeCompConstParser.SimpleContext ctx) {
        if (ctx.factor(0) != null && ctx.factor(1) == null) {
            return visit(ctx.factor(0));
        }

        visit(ctx.factor(0));

        visit(ctx.factor(1));

        if (ctx.TIMES() != null) {
            routine.body.append("imul").append("\n");
        } else if (ctx.SLASH() != null) {
            routine.body.append("idiv").append("\n");
        } else if (ctx.PERCENT() != null) {
            routine.body.append("irem").append("\n");
        }
        return null;
    }

    @Override
    public Void visitFactor(ImperativeCompConstParser.FactorContext ctx) {
        if (ctx.summand(0) != null && ctx.summand(1) == null) {
            return visit(ctx.summand(0));
        }
        visit(ctx.summand(0));

        visit(ctx.summand(1));

        if (ctx.PLUS() != null) {
            routine.body.append("iadd").append("\n");
        } else if (ctx.MINUS() != null) {
            routine.body.append("isub").append("\n");
        }
        return null;
    }

    @Override
    public Void visitSummand(ImperativeCompConstParser.SummandContext ctx) {
        if (ctx.primary() != null) {
            return visit(ctx.primary());
        }
        return visit(ctx.expression());
    }

    @Override
    public Void visitPrimary(ImperativeCompConstParser.PrimaryContext ctx) {
        if (ctx.INT() != null) {
            String intValue = ctx.INT().getText();
            routine.body.append("ldc " + intValue).append("\n");
        } else if (ctx.REAL() != null) {
            String realValue = ctx.REAL().getText();
            routine.body.append("ldc " + realValue).append("\n");
        } else if (ctx.TRUE() != null) {
            routine.body.append("iconst_1").append("\n");
        } else if (ctx.FALSE() != null) {
            routine.body.append("iconst_0").append("\n");
        } else if (ctx.modifiable_primary() != null) {
            visit(ctx.modifiable_primary());
        }
        return null;
    }

    @Override
    public Void visitModifiable_primary(ImperativeCompConstParser.Modifiable_primaryContext ctx) {

        List<String> accessChain = new ArrayList<>();
        ImperativeCompConstParser.Modifiable_primaryContext currentCtx = ctx;

        while (currentCtx != null) {
            if (currentCtx.IDENT() != null) {

                accessChain.addFirst(currentCtx.IDENT().getText());
            }

            currentCtx = currentCtx.modifiable_primary();
        }


        String baseVarName = accessChain.removeFirst();
        Helper baseVarHelper = variableTable.get(baseVarName);
        if (baseVarHelper == null) {
            throw new IllegalStateException("Variable '" + baseVarName + "' is not defined");
        }

        String currentType = baseVarHelper.type;


        int baseVarIdx = baseVarHelper.idx;
        switch (currentType) {
            case "I" -> routine.body.append("iload " + baseVarIdx + " ; load integer variable").append("\n");
            case "F" -> routine.body.append("fload " + baseVarIdx + " ; load float variable").append("\n");
            case "Z" -> routine.body.append("iload " + baseVarIdx + " ; load boolean variable").append("\n");
            default -> routine.body.append("aload " + baseVarIdx + " ; load reference variable").append("\n");
        }


        for (String fieldName : accessChain) {

            UserDefinedType recordType = userDefinedTypes.get(currentType);
            if (recordType == null) {
                throw new IllegalStateException("Type '" + currentType + "' is not a user-defined record");
            }


            String fieldType = recordType.fields.get(fieldName);
            if (fieldType == null) {
                throw new IllegalStateException("Field '" + fieldName + "' is not defined in record type '" + currentType + "'");
            }


            routine.body.append("getfield " + currentType + "/" + fieldName + " " + mapType(fieldType)).append("\n");


            currentType = fieldType;
        }

        return null;
    }

    @Override
    public Void visitVariable_declaration(ImperativeCompConstParser.Variable_declarationContext ctx) {
        String varName = ctx.IDENT().getText();
        String varType = ctx.type().getText();

        int idx = addLocal(varName, mapType(varType));
        if (ctx.expression() == null) return null;
        visit(ctx.expression());
        switch (varType) {
            case "integer" -> routine.body.append("istore " + idx + " ; store integer variable").append("\n");
            case "real" -> routine.body.append("fstore " + idx + " ; store float variable").append("\n");
            case "boolean" -> routine.body.append("istore " + idx + " ; store boolean variable").append("\n");
            default -> routine.body.append("astore " + idx + " ; store reference variable").append("\n");
        }
        return null;
    }

    @Override
    public Void visitAssignment(ImperativeCompConstParser.AssignmentContext ctx) {
        if (ctx.modifiable_primary().modifiable_primary() != null) {
            List<String> accessChain = new ArrayList<>();
            ImperativeCompConstParser.Modifiable_primaryContext currentCtx = ctx.modifiable_primary();

            while (currentCtx != null) {
                if (currentCtx.IDENT() != null) {

                    accessChain.addFirst(currentCtx.IDENT().getText());
                }
                currentCtx = currentCtx.modifiable_primary();
            }


            String baseVarName = accessChain.removeFirst();
            Helper baseVarHelper = variableTable.get(baseVarName);
            if (baseVarHelper == null) {
                throw new RuntimeException("Variable not declared: " + baseVarName);
            }

            String currentType = baseVarHelper.type;


            int baseVarIdx = baseVarHelper.idx;
            routine.body.append("aload " + baseVarIdx + " ; load reference variable").append("\n");


            for (int i = 0; i < accessChain.size() - 1; i++) {
                String fieldName = accessChain.get(i);


                UserDefinedType recordType = userDefinedTypes.get(currentType);
                if (recordType == null) {
                    throw new RuntimeException("Type '" + currentType + "' is not a user-defined record");
                }


                String fieldType = recordType.fields.get(fieldName);
                if (fieldType == null) {
                    throw new RuntimeException("Field '" + fieldName + "' is not defined in record type '" + currentType + "'");
                }


                routine.body.append("getfield " + currentType + "/" + fieldName + " " + mapType(fieldType)).append("\n");


                currentType = fieldType;
            }


            visit(ctx.expression() != null ? ctx.expression() : ctx.routine_call());


            String finalFieldName = accessChain.getLast();


            UserDefinedType recordType = userDefinedTypes.get(currentType);
            if (recordType == null) {
                throw new RuntimeException("Type '" + currentType + "' is not a user-defined record");
            }


            String finalFieldType = recordType.fields.get(finalFieldName);
            if (finalFieldType == null) {
                throw new RuntimeException("Field '" + finalFieldName + "' is not defined in record type '" + currentType + "'");
            }


            routine.body.append("putfield " + currentType + "/" + finalFieldName + " " + mapType(finalFieldType)).append("\n");

        } else {
            visit(ctx.expression() != null ? ctx.expression() : ctx.routine_call());
            String varName = ctx.modifiable_primary().IDENT().getText();
            Helper varHelper = variableTable.get(varName);

            switch (varHelper.type) {
                case "integer" -> routine.body.append("istore " + varHelper.idx + " ; store integer variable").append("\n");
                case "real" -> routine.body.append("fstore " + varHelper.idx + " ; store float variable").append("\n");
                case "boolean" -> routine.body.append("istore " + varHelper.idx + " ; store boolean variable").append("\n");
                default -> routine.body.append("astore " + varHelper.idx + " ; store reference variable").append("\n");
            }

        }
        return null;
    }

    @Override
    public Void visitWhile_loop(ImperativeCompConstParser.While_loopContext ctx) {
        var startLoopLabel = generateLabel();
        var endLoopLabel = generateLabel();
        routine.body.append(startLoopLabel + ":").append("\n");
        visit(ctx.expression());
        routine.body.append("ifeq " + endLoopLabel).append("\n");
        visit(ctx.body());

        routine.body.append("goto " + startLoopLabel).append("\n");
        routine.body.append(endLoopLabel + ":").append("\n");
        return null;
    }

    @Override
    public Void visitFor_loop(ImperativeCompConstParser.For_loopContext ctx) {
        var varName = ctx.IDENT().getText();
        var variableHelper = variableTable.get(varName);
        if (variableHelper == null) {
            throw new RuntimeException("Variable not declared: " + varName);
        }
        int idx = variableHelper.idx;

        visit(ctx.range().expression(0));
        routine.body.append("istore " + idx).append("\n");

        int endIndex = idx + 1;
        visit(ctx.range().expression(1));
        routine.body.append("istore " + endIndex).append("\n");

        String loopStartLabel = "loop_start_" + idx;
        String endLoopLabel = "end_loop_" + idx;

        routine.body.append(loopStartLabel + ":").append("\n");
        routine.body.append("iload " + idx).append("\n");
        routine.body.append("iload " + endIndex).append("\n");
        routine.body.append("if_icmpge " + endLoopLabel).append("\n");

        visit(ctx.body());

        routine.body.append("iinc " + idx + " 1").append("\n");

        routine.body.append("goto " + loopStartLabel).append("\n");

        routine.body.append(endLoopLabel + ":").append("\n");

        return null;
    }

    @Override
    public Void visitIf_statement(ImperativeCompConstParser.If_statementContext ctx) {

        visit(ctx.expression());


        String elseLabel = generateLabel();
        String endLabel = generateLabel();


        routine.body.append("ifeq " + elseLabel).append("\n");


        visit(ctx.body(0));


        routine.body.append("goto " + endLabel).append("\n");


        routine.body.append(elseLabel + ":").append("\n");

        if (ctx.ELSESYM() != null) {

            visit(ctx.body(1));
        }


        routine.body.append(endLabel + ":").append("\n");

        return null;
    }

    @Override
    public Void visitReturn_expression(ImperativeCompConstParser.Return_expressionContext ctx) {
        if (routine.returnType.equals("V")) {
            throw new IllegalStateException("Function should not return anything but return statement found");
        }
        // TODO: check that inferred type of expression is compatible
        visit(ctx.expression());
        String returnCmd = switch (routine.returnType) {
            case "I", "Z" -> "ireturn";
            case "F" -> "freturn";
            default -> "areturn";
        };
        routine.body.append(returnCmd).append("\n");
        return null;
    }
}
