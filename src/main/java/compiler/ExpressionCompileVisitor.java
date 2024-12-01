package compiler;

import gen.ImperativeCompConstParser;

import java.util.ArrayList;
import java.util.List;

public class ExpressionCompileVisitor extends BaseCompileVisitor {
    @Override
    public Void visitExpression(ImperativeCompConstParser.ExpressionContext ctx) {
        if (ctx.relation() != null && ctx.relation().size() == 1) {
            // Handle a simple relation (comparison or value)
            visit(ctx.relation(0));
        } else if (ctx.relation(0) != null && ctx.AND() != null) {
            // Handle logical AND
            visit(ctx.relation(0));
            visit(ctx.relation(1));
            appendln("iand");
        } else if (ctx.relation(0) != null && ctx.OR() != null) {
            // Handle logical OR
            visit(ctx.relation(0));
            visit(ctx.relation(1));
            appendln("ior");
        } else if (ctx.relation(0) != null && ctx.XOR() != null) {
            // Handle logical XOR
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
        // First check if the relation is just a simple expression
        if (ctx.simple(0) != null && ctx.simple(1) == null) {
            // Visit the simple expression
            visit(ctx.simple(0));
        } else if (ctx.simple(0) != null && ctx.simple(1) != null) {
            // Visit the left-hand side (simple)
            visit(ctx.simple(0));

            // Visit the right-hand side (simple)
            visit(ctx.simple(1));

            // Check which comparison operator we're dealing with and emit the corresponding Jasmin code
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

            appendln("iconst_0"); // Push 0 (false) to stack if not true
            appendln("goto " + labelEnd);

            appendln(labelTrue + ":");
            appendln("iconst_1"); // Push 1 (true) to stack

            appendln(labelEnd + ":");
        }
        return null;
    }

    @Override
    public Void visitSimple(ImperativeCompConstParser.SimpleContext ctx) {
        if (ctx.factor(0) != null && ctx.factor(1) == null) {
            // Visit single factor
            visit(ctx.factor(0));
        } else if (ctx.factor(0) != null && ctx.factor(1) != null) {
            // Visit left and right factors
            visit(ctx.factor(0));
            visit(ctx.factor(1));

            // Determine the types of the operands (assuming they're the same for simplicity)
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
            // Visit single summand
            visit(ctx.summand(0));
        } else if (ctx.summand(0) != null && ctx.summand(1) != null) {
            // Visit left and right summands
            visit(ctx.summand(0));
            visit(ctx.summand(1));

            // Determine the types of the operands
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
        // If the summand is a primary
        if (ctx.primary() != null) {
            visit(ctx.primary());
        }
        // If the summand is an expression inside parentheses
        else if (ctx.LPAREN() != null && ctx.RPAREN() != null) {
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
        // Start by flattening the access chain into a list
        List<String> accessChain = new ArrayList<>();
        ImperativeCompConstParser.Modifiable_primaryContext currentCtx = ctx;

        while (currentCtx != null) {
            if (currentCtx.IDENT() != null) {
                // Add the identifier (field or variable) to the access chain
                accessChain.addFirst(currentCtx.IDENT().getText());
            }
            // Traverse up the parse tree to process the next part of the chain
            currentCtx = currentCtx.modifiable_primary();
        }

        // Resolve the base variable
        String baseVarName = accessChain.removeFirst();
        Helper baseVarHelper = variableTable.get(baseVarName);
        if (baseVarHelper == null) {
            throw new IllegalStateException("Variable '" + baseVarName + "' is not defined");
        }

        String currentType = baseVarHelper.type;

        // Load the base variable onto the stack
        int baseVarIdx = baseVarHelper.idx;
        switch (currentType) {
            case "integer" -> appendln("iload " + baseVarIdx + " ; load integer variable");
            case "real" -> appendln("fload " + baseVarIdx + " ; load float variable");
            case "boolean" -> appendln("iload " + baseVarIdx + " ; load boolean variable");
            default -> appendln("aload " + baseVarIdx + " ; load reference variable");
        }

        // Iteratively resolve each field in the chain
        for (String fieldName : accessChain) {
            // Ensure the current type is a record
            UserDefinedType recordType = userDefinedTypes.get(currentType);
            if (recordType == null) {
                throw new IllegalStateException("Type '" + currentType + "' is not a user-defined record");
            }

            // Get the field type
            String fieldType = recordType.fields.get(fieldName);
            if (fieldType == null) {
                throw new IllegalStateException("Field '" + fieldName + "' is not defined in record type '" + currentType + "'");
            }

            // Emit code to access the field
            appendln("getfield " + currentType + "/" + fieldName + " " + mapType(fieldType));

            // Update the current type to the field type
            currentType = fieldType;
        }

        return null;
    }
}
