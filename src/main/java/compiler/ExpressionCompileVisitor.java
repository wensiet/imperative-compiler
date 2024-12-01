package compiler;

import gen.ImperativeCompConstParser;

import java.util.ArrayList;
import java.util.List;

public class ExpressionCompileVisitor extends BaseCompileVisitor {
    @Override
    public Void visitExpression(ImperativeCompConstParser.ExpressionContext ctx) {
        if (ctx.relation() != null && ctx.relation().size() == 1) {
            visit(ctx.relation(0));
        } else if (ctx.relation(0) != null && ctx.AND() != null) {
            visit(ctx.relation(0));
            visit(ctx.relation(1));
            appendln("iand");
        } else if (ctx.relation(0) != null && ctx.OR() != null) {
            visit(ctx.relation(0));
            visit(ctx.relation(1));
            appendln("ior");
        } else if (ctx.relation(0) != null && ctx.XOR() != null) {
            visit(ctx.relation(0));
            visit(ctx.relation(1));
            appendln("ixor");
        } else if (ctx.routine_call() != null) {
            visit(ctx.routine_call());
        }
        return null;
    }

    @Override
    public Void visitRelation(ImperativeCompConstParser.RelationContext ctx) {
        if (ctx.simple(0) != null && ctx.simple(1) == null) {
            visit(ctx.simple(0));
        } else if (ctx.simple(0) != null && ctx.simple(1) != null) {
            visit(ctx.simple(0));

            visit(ctx.simple(1));

            String labelTrue = generateLabel();
            if (ctx.LSS() != null) {
                appendln("if_icmplt " + labelTrue); // less than (LSS)
            } else if (ctx.LEQ() != null) {
                appendln("if_icmple " + labelTrue); // less than or equal to (LEQ)
            } else if (ctx.GTR() != null) {
                appendln("if_icmpgt " + labelTrue); // greater than (GTR)
            } else if (ctx.GEQ() != null) {
                appendln("if_icmpge " + labelTrue); // greater than or equal to (GEQ)
            } else if (ctx.EQL() != null) {
                appendln("if_icmpeq " + labelTrue); // equal to (EQL)
            } else if (ctx.NEQ() != null) {
                appendln("if_icmpne " + labelTrue); // not equal to (NEQ)
            }

            String labelEnd = generateLabel();

            appendln("iconst_0");
            appendln("goto " + labelEnd);

            appendln(labelTrue + ":");
            appendln("iconst_1");

            appendln(labelEnd + ":");
        }
        return null;
    }

    @Override
    public Void visitSimple(ImperativeCompConstParser.SimpleContext ctx) {
        if (ctx.factor(0) != null && ctx.factor(1) == null) {
            visit(ctx.factor(0));
        } else if (ctx.factor(0) != null && ctx.factor(1) != null) {
            visit(ctx.factor(0));
            visit(ctx.factor(1));

            String leftType = getExpressionType(ctx.factor(0));
            String rightType = getExpressionType(ctx.factor(1));

            if (!leftType.equals(rightType)) {
                throw new IllegalStateException("Type mismatch: " + leftType + " and " + rightType);
            }

            switch (leftType) {
                case "integer" -> {
                    if (ctx.TIMES() != null) appendln("imul");
                    else if (ctx.SLASH() != null) appendln("idiv");
                    else if (ctx.PERCENT() != null) appendln("irem");
                }
                case "real" -> {
                    if (ctx.TIMES() != null) appendln("fmul");
                    else if (ctx.SLASH() != null) appendln("fdiv");
                    else throw new IllegalStateException("Modulo not supported for real numbers");
                }
                default -> throw new IllegalStateException("Unsupported type for operation: " + leftType);
            }
        }
        return null;
    }

    @Override
    public Void visitFactor(ImperativeCompConstParser.FactorContext ctx) {
        if (ctx.summand(0) != null && ctx.summand(1) == null) {
            visit(ctx.summand(0));
        } else if (ctx.summand(0) != null && ctx.summand(1) != null) {
            visit(ctx.summand(0));
            visit(ctx.summand(1));

            String leftType = getExpressionType(ctx.summand(0));
            String rightType = getExpressionType(ctx.summand(1));

            if (!leftType.equals(rightType)) {
                throw new IllegalStateException("Type mismatch: " + leftType + " and " + rightType);
            }

            switch (leftType) {
                case "integer" -> {
                    if (ctx.PLUS() != null) appendln("iadd");
                    else if (ctx.MINUS() != null) appendln("isub");
                }
                case "real" -> {
                    if (ctx.PLUS() != null) appendln("fadd");
                    else if (ctx.MINUS() != null) appendln("fsub");
                }
                default -> throw new IllegalStateException("Unsupported type for operation: " + leftType);
            }
        }
        return null;
    }

    @Override
    public Void visitSummand(ImperativeCompConstParser.SummandContext ctx) {
        if (ctx.primary() != null) {
            visit(ctx.primary());
        } else if (ctx.LPAREN() != null && ctx.RPAREN() != null) {
            visit(ctx.expression());
        }
        return null;
    }

    @Override
    public Void visitPrimary(ImperativeCompConstParser.PrimaryContext ctx) {
        if (ctx.INT() != null) {
            String intValue = ctx.INT().getText();
            appendln("ldc " + intValue);
        } else if (ctx.REAL() != null) {
            String realValue = ctx.REAL().getText();
            appendln("ldc " + realValue);
        } else if (ctx.TRUE() != null) {
            appendln("iconst_1");
        } else if (ctx.FALSE() != null) {
            appendln("iconst_0");
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
            case "integer" -> appendln("iload " + baseVarIdx + " ; load integer variable");
            case "real" -> appendln("fload " + baseVarIdx + " ; load float variable");
            case "boolean" -> appendln("iload " + baseVarIdx + " ; load boolean variable");
            default -> appendln("aload " + baseVarIdx + " ; load reference variable");
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

            appendln("getfield " + currentType + "/" + fieldName + " " + mapType(fieldType));

            currentType = fieldType;
        }

        if (ctx.expression() != null) {
            visitExpression(ctx.expression());
        }
        if (currentType.contains("array")) {
            if (currentType.contains("integer")) {
                appendln("iaload" + " ; integer array load");
            } else if (currentType.contains("real")) {
                appendln("faload" + " ; float array load");
            } else if (currentType.contains("bool")) {
                appendln("baload" + " ; boolean array load");
            }
        }
        return null;
    }
}
