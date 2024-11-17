package compiler;

import gen.ImperativeCompConstParser;

public class ExpressionCompileVisitor extends BaseCompileVisitor {
    @Override
    public Void visitExpression(ImperativeCompConstParser.ExpressionContext ctx) {
        if (ctx.relation() != null) {
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
            if (ctx.LSS() != null) {
                appendln("if_icmplt label_true"); // less than (LSS)
            } else if (ctx.LEQ() != null) {
                appendln("if_icmple label_true"); // less than or equal to (LEQ)
            } else if (ctx.GTR() != null) {
                appendln("if_icmpgt label_true"); // greater than (GTR)
            } else if (ctx.GEQ() != null) {
                appendln("if_icmpge label_true"); // greater than or equal to (GEQ)
            } else if (ctx.EQL() != null) {
                appendln("if_icmpeq label_true"); // equal to (EQL)
            } else if (ctx.NEQ() != null) {
                appendln("if_icmpne label_true"); // not equal to (NEQ)
            }

            // After the comparison, if the condition is true, jump to the label "label_true"
            appendln("iconst_0"); // Push 0 (false) to stack if not true
            appendln("goto label_end");

            // Label for the true case
            appendln("label_true:");
            appendln("iconst_1"); // Push 1 (true) to stack

            // End label
            appendln("label_end:");
        }
        return null;
    }

    @Override
    public Void visitSimple(ImperativeCompConstParser.SimpleContext ctx) {
        // First check if it's just a single factor (no operator)
        if (ctx.factor(0) != null && ctx.factor(1) == null) {
            // Visit the first (and only) factor
            visit(ctx.factor(0));
        }
        // If it's a multiplication, division, or modulo
        else if (ctx.factor(0) != null && ctx.factor(1) != null) {
            // Visit the left-hand side (first factor)
            visit(ctx.factor(0));

            // Visit the right-hand side (second factor)
            visit(ctx.factor(1));

            // Handle the operator based on the token (TIMES, SLASH, PERCENT)
            if (ctx.TIMES() != null) {
                appendln("imul");  // multiplication
            } else if (ctx.SLASH() != null) {
                appendln("idiv");  // division
            } else if (ctx.PERCENT() != null) {
                appendln("irem");  // modulo
            }
        }
        return null;
    }

    @Override
    public Void visitFactor(ImperativeCompConstParser.FactorContext ctx) {
        // First check if it's just a single summand (no operator)
        if (ctx.summand(0) != null && ctx.summand(1) == null) {
            // Visit the first (and only) summand
            visit(ctx.summand(0));
        }
        // If it's an addition or subtraction operation
        else if (ctx.summand(0) != null && ctx.summand(1) != null) {
            // Visit the left-hand side (first summand)
            visit(ctx.summand(0));

            // Visit the right-hand side (second summand)
            visit(ctx.summand(1));

            // Handle the operator based on the token (PLUS or MINUS)
            if (ctx.PLUS() != null) {
                appendln("iadd");  // addition
            } else if (ctx.MINUS() != null) {
                appendln("isub");  // subtraction
            }
        }
        return null;
    }

    @Override
    public Void visitSummand(ImperativeCompConstParser.SummandContext ctx) {
        // If the summand is a primary
        if (ctx.primary() != null) {
            visit(ctx.primary());  // Visit the primary expression (e.g., variable or constant)
        }
        // If the summand is an expression inside parentheses
        else if (ctx.LPAREN() != null && ctx.RPAREN() != null) {
            visit(ctx.expression());  // Visit the expression inside parentheses
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
        if (ctx.IDENT() != null) {
            String variableName = ctx.IDENT().getText();
            Helper helper = variableTable.get(variableName);

            if (helper == null) {
                throw new IllegalStateException("Variable '" + variableName + "' is not defined");
            }

            String type = helper.type;
            Integer idx = helper.idx;

            switch (type) {
                case "integer" -> appendln("iload " + idx + " ; load integer variable");
                case "float" -> appendln("fload " + idx + " ; load float variable");
                case "boolean" -> appendln("iload " + idx + " ; load boolean variable");
                default -> appendln("aload " + idx + " ; load reference variable");
            }
        } else if (ctx.LBRACKET() != null && ctx.RBRACKET() != null) {
            visit(ctx.modifiable_primary());
            visit(ctx.expression());
            appendln("iaload ; array element load");
        } else if (ctx.PERIOD() != null) {
            visit(ctx.modifiable_primary());
            String fieldName = ctx.IDENT().getText();
            appendln("getfield <TODO_CLASS_NAME>/" + fieldName + " TODO_DESCRIPTOR ; field access");
        }
        return null;
    }
}
