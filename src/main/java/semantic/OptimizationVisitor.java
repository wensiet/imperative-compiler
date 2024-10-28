package semantic;

import gen.ImperativeCompConstBaseVisitor;
import gen.ImperativeCompConstParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.antlr.v4.runtime.CommonToken;

public class OptimizationVisitor extends ImperativeCompConstBaseVisitor<Object> {
    @Override
    public Object visitAssignment(ImperativeCompConstParser.AssignmentContext ctx) {
        System.out.println("sjdadd");
        Object value = visit(ctx.expression());
        if (value instanceof Integer || value instanceof Double || value instanceof Boolean) {
            ctx.expression().getParent().removeLastChild();
            ctx.expression().getParent().addChild(new TerminalNodeImpl(new CommonToken(getTokenType(value), value.toString())));
        }
        return null;

        /*
        (input (program (simple_declaration (variable_declaration var a : (type (primitive_type integer)) is (expression (relation (simple (factor (summand (primary 1)) + (summand (primary 1)))))) ;))) <EOF>)

        (input (program (simple_declaration (variable_declaration var a : (type (primitive_type integer)) is (expression (relation (simple (factor (summand (primary 2)))))) ;))) <EOF>)

         */
    }

    @Override
    public Object visitSimple_declaration(ImperativeCompConstParser.Simple_declarationContext ctx) {
        System.out.println("simple declaration");
        System.out.println(ctx.getText());
        System.out.println(ctx.getChildCount());
        return visitChildren(ctx);
    }

//    @Override
//    public Object visitRelation(ImperativeCompConstParser.RelationContext ctx) {
//        System.out.println(ctx.getChildCount());
//        return visitChildren(ctx);
//    }


    @Override
    public Object visitExpression(ImperativeCompConstParser.ExpressionContext ctx) {
        if (ctx.getChildCount() == 3) {
            Object left = visit(ctx.relation(0));
            Object right = visit(ctx.relation(1));
            if (left instanceof Integer && right instanceof Integer) {
                int result = 0;
                String operator = ctx.getChild(1).getText();
                switch (operator) {
                    case "+":
                        result = (Integer) left + (Integer) right;
                        break;
                    case "-":
                        result = (Integer) left - (Integer) right;
                        break;
                    case "*":
                        result = (Integer) left * (Integer) right;
                        break;
                    case "/":
                        if ((Integer) right != 0) {
                            result = (Integer) left / (Integer) right;
                        } else {
                            throw new ArithmeticException("Division by zero");
                        }
                        break;
                    case "<":
                        result = ((Integer) left < (Integer) right) ? 1 : 0;
                        break;
                    case ">":
                        result = ((Integer) left > (Integer) right) ? 1 : 0;
                        break;
                    case "=":
                        result = ((Integer) left == (Integer) right) ? 1 : 0;
                        break;
                }
                // Replace the expression with the simplified constant
                ctx.getParent().removeLastChild();
                ctx.getParent().addChild(new TerminalNodeImpl(new CommonToken(getTokenType(result), Integer.toString(result))));
                return result;
            } else if (left instanceof Double && right instanceof Double) {
                double result = 0;
                String operator = ctx.getChild(1).getText();
                switch (operator) {
                    case "+":
                        result = (Double) left + (Double) right;
                        break;
                    case "-":
                        result = (Double) left - (Double) right;
                        break;
                    case "*":
                        result = (Double) left * (Double) right;
                        break;
                    case "/":
                        if ((Double) right != 0) {
                            result = (Double) left / (Double) right;
                        } else {
                            throw new ArithmeticException("Division by zero");
                        }
                        break;
                    case "<":
                        result = ((Double) left < (Double) right) ? 1 : 0;
                        break;
                    case ">":
                        result = ((Double) left > (Double) right) ? 1 : 0;
                        break;
                    case "=":
                        result = ((Double) left == (Double) right) ? 1 : 0;
                        break;
                }
                // Replace the expression with the simplified constant
                ctx.getParent().removeLastChild();
                ctx.getParent().addChild(new TerminalNodeImpl(new CommonToken(getTokenType(result), Double.toString(result))));
                return result;
            } else if (left instanceof Boolean && right instanceof Boolean) {
                boolean result = false;
                String operator = ctx.getChild(1).getText();
                switch (operator) {
                    case "and":
                        result = (Boolean) left && (Boolean) right;
                        break;
                    case "or":
                        result = (Boolean) left || (Boolean) right;
                        break;
                    case "xor":
                        result = (Boolean) left ^ (Boolean) right;
                        break;
                }
                // Replace the expression with the simplified constant
                ctx.getParent().removeLastChild();
                ctx.getParent().addChild(new TerminalNodeImpl(new CommonToken(getTokenType(result), Boolean.toString(result))));
                return result;
            }
        } else if (ctx.getChildCount() == 1) { // Terminal node (NUMBER, REAL, TRUE, FALSE)
            if (ctx.getChild(0).getText().equals("true")) {
                return true;
            } else if (ctx.getChild(0).getText().equals("false")) {
                return false;
            } else if (ctx.getChild(0).getText().matches("[0-9]+")) {
                return Integer.parseInt(ctx.getChild(0).getText());
            } else if (ctx.getChild(0).getText().matches("[0-9]+[.][0-9]+")) {
                return Double.parseDouble(ctx.getChild(0).getText());
            }
        }
        return null;
    }

    private int getTokenType(Object value) {
        if (value instanceof Integer) {
            return ImperativeCompConstParser.INT;
        } else if (value instanceof Double) {
            return ImperativeCompConstParser.REAL;
        } else if (value instanceof Boolean) {
            return ImperativeCompConstParser.BOOL_TYPE;
        }
        return ImperativeCompConstParser.IDENT;
    }
}