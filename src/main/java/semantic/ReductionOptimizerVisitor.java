package semantic;

import gen.ImperativeCompConstBaseVisitor;
import gen.ImperativeCompConstParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.antlr.v4.runtime.CommonToken;


public class ReductionOptimizerVisitor extends ImperativeCompConstBaseVisitor<Object> {

    @Override
    public Object visitFactor(ImperativeCompConstParser.FactorContext ctx) {
        if (ctx.getChildCount() == 3) {
            ParseTree left = ctx.getChild(0);
            ParseTree operator = ctx.getChild(1);
            ParseTree right = ctx.getChild(2);

            ImperativeCompConstParser.PrimaryContext leftPrimary = ((ImperativeCompConstParser.SummandContext) left).primary();
            ImperativeCompConstParser.PrimaryContext rightPrimary = ((ImperativeCompConstParser.SummandContext) right).primary();

            if ((leftPrimary.INT() != null && rightPrimary.INT() != null) || (leftPrimary.REAL() != null && rightPrimary.REAL() != null)) {

                if (leftPrimary.INT() != null && rightPrimary.INT() != null) {
                    int leftValue = Integer.parseInt(leftPrimary.INT().getText());
                    int rightValue = Integer.parseInt(rightPrimary.INT().getText());
                    int result;

                    switch (operator.getText()) {
                        case "+":
                            result = leftValue + rightValue;
                            break;
                        case "-":
                            result = leftValue - rightValue;
                            break;
                        default:
                            return visitChildren(ctx);
                    }

                    ImperativeCompConstParser.PrimaryContext newPrimary = new ImperativeCompConstParser.PrimaryContext(ctx, ctx.invokingState);
                    TerminalNodeImpl newIntNode = new TerminalNodeImpl(new CommonToken(ImperativeCompConstParser.INT, Integer.toString(result)));
                    newPrimary.addChild(newIntNode);

                    while (ctx.getChildCount() > 0) {
                        ctx.removeLastChild();
                    }

                    ImperativeCompConstParser.SummandContext newSummand = new ImperativeCompConstParser.SummandContext(ctx, ctx.invokingState);
                    newSummand.addChild(newPrimary);
                    ctx.addAnyChild(newSummand);

                    return null;
                } else if (leftPrimary.REAL() != null && rightPrimary.REAL() != null) {
                    double leftValue = Double.parseDouble(leftPrimary.REAL().getText());
                    double rightValue = Double.parseDouble(rightPrimary.REAL().getText());
                    double result;

                    switch (operator.getText()) {
                        case "+":
                            result = leftValue + rightValue;
                            break;
                        case "-":
                            result = leftValue - rightValue;
                            break;
                        default:
                            return visitChildren(ctx);
                    }

                    ImperativeCompConstParser.PrimaryContext newPrimary = new ImperativeCompConstParser.PrimaryContext(ctx, ctx.invokingState);
                    TerminalNodeImpl newRealNode = new TerminalNodeImpl(new CommonToken(ImperativeCompConstParser.REAL, Double.toString(result)));
                    newPrimary.addChild(newRealNode);

                    while (ctx.getChildCount() > 0) {
                        ctx.removeLastChild();
                    }

                    ImperativeCompConstParser.SummandContext newSummand = new ImperativeCompConstParser.SummandContext(ctx, ctx.invokingState);
                    newSummand.addChild(newPrimary);
                    ctx.addAnyChild(newSummand);

                    return null;
                }
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public Object visitSimple(ImperativeCompConstParser.SimpleContext ctx) {
        if (ctx.getChildCount() == 3) {
            ParseTree leftFactor = ctx.getChild(0);
            ParseTree operator = ctx.getChild(1);
            ParseTree rightFactor = ctx.getChild(2);

            ImperativeCompConstParser.PrimaryContext leftPrimary = ((ImperativeCompConstParser.SummandContext) leftFactor.getChild(0)).primary();
            ImperativeCompConstParser.PrimaryContext rightPrimary = ((ImperativeCompConstParser.SummandContext) rightFactor.getChild(0)).primary();
            if (leftPrimary.INT() != null && rightPrimary.INT() != null) {
                int leftValue = Integer.parseInt(leftPrimary.INT().getText());
                int rightValue = Integer.parseInt(rightPrimary.INT().getText());
                Integer result = computeIntegerResult(leftValue, rightValue, operator.getText());
                if (result != null)
                    return replaceContextWithResult(ctx, result.toString(), ImperativeCompConstParser.INT);

            } else if (leftPrimary.REAL() != null && rightPrimary.REAL() != null) {
                double leftValue = Double.parseDouble(leftPrimary.REAL().getText());
                double rightValue = Double.parseDouble(rightPrimary.REAL().getText());
                Double result = computeDoubleResult(leftValue, rightValue, operator.getText());
                if (result != null)
                    return replaceContextWithResult(ctx, result.toString(), ImperativeCompConstParser.REAL);

            }
        }
        return visitChildren(ctx);
    }


    private Integer computeIntegerResult(int left, int right, String operator) {
        return switch (operator) {
            case "*" -> left * right;
            case "/" -> right != 0 ? left / right : null;
            default -> null;
        };
    }

    private Double computeDoubleResult(double left, double right, String operator) {
        return switch (operator) {
            case "*" -> left * right;
            case "/" -> right != 0.0 ? left / right : null;
            default -> null;
        };
    }

    private Object replaceContextWithResult(ImperativeCompConstParser.SimpleContext ctx, String result, int tokenType) {
        ImperativeCompConstParser.PrimaryContext newPrimary = new ImperativeCompConstParser.PrimaryContext(ctx, ctx.invokingState);
        TerminalNodeImpl newNode = new TerminalNodeImpl(new CommonToken(tokenType, result));
        newPrimary.addChild(newNode);

        while (ctx.getChildCount() > 0) {
            ctx.removeLastChild();
        }

        ImperativeCompConstParser.SummandContext newSummand = new ImperativeCompConstParser.SummandContext(ctx, ctx.invokingState);
        newSummand.addChild(newPrimary);

        ImperativeCompConstParser.FactorContext newFactor = new ImperativeCompConstParser.FactorContext(ctx, ctx.invokingState);
        newFactor.addChild(newSummand);

        ctx.addAnyChild(newFactor);

        return null;
    }
}