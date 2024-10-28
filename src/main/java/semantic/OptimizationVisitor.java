package semantic;

import gen.ImperativeCompConstBaseVisitor;
import gen.ImperativeCompConstParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.antlr.v4.runtime.CommonToken;

import java.util.Objects;

public class OptimizationVisitor extends ImperativeCompConstBaseVisitor<Object> {
//    @Override
//    public Object visitAssignment(ImperativeCompConstParser.AssignmentContext ctx) {
//        System.out.println("visit assignment");
//        Object value = visit(ctx.expression());
//        if (value instanceof Integer || value instanceof Double || value instanceof Boolean) {
//            ctx.expression().getParent().removeLastChild();
//            ctx.expression().getParent().addChild(new TerminalNodeImpl(new CommonToken(getTokenType(value), value.toString())));
//        }
//        return null;
//
//        /*
//        (input (program (simple_declaration (variable_declaration var a : (type (primitive_type integer)) is (expression (relation (simple (factor (summand (primary 1)) + (summand (primary 1)))))) ;))) <EOF>)
//
//        (input (program (simple_declaration (variable_declaration var a : (type (primitive_type integer)) is (expression (relation (simple (factor (summand (primary 2)))))) ;))) <EOF>)
//
//         */
//    }

//    @Override
//    public Object visitSimple_declaration(ImperativeCompConstParser.Simple_declarationContext ctx) {
//        System.out.println("simple declaration");
//        System.out.println(ctx.getText());
//        System.out.println(ctx.getChildCount());
//        return visitChildren(ctx);
//    }


//    @Override
//    public Object visitSimple(ImperativeCompConstParser.SimpleContext ctx) {
//        System.out.println(ctx.getChildCount());
//        System.out.println(ctx.getChild(0).getText());
//        return visitChildren(ctx);
//    }

    @Override
    public Object visitFactor(ImperativeCompConstParser.FactorContext ctx) {

        if (ctx.getChildCount() == 3) {
            ParseTree left = ctx.getChild(0);
            ParseTree operator = ctx.getChild(1);
            ParseTree right = ctx.getChild(2);
            /*
            (input (program (simple_declaration (variable_declaration var a : (type (primitive_type integer)) is (expression (relation (simple (factor (summand (primary 1)) + (summand (primary 1)))))) ;))) <EOF>)


            (input (program (simple_declaration (variable_declaration var a : (type (primitive_type integer)) is (expression (relation (simple (factor (summand (primary 2)))))) ;))) <EOF>)
             */

            if (left instanceof ImperativeCompConstParser.SummandContext && right instanceof ImperativeCompConstParser.SummandContext) {
                ImperativeCompConstParser.PrimaryContext leftPrimary = ((ImperativeCompConstParser.SummandContext) left).primary();
                ImperativeCompConstParser.PrimaryContext rightPrimary = ((ImperativeCompConstParser.SummandContext) right).primary();

                if (isIntOrReal(leftPrimary) && isIntOrReal(rightPrimary) && Objects.equals(leftPrimary.getText(), rightPrimary.getText())) {
                    int leftValue = Integer.parseInt(leftPrimary.INT().getText());
                    int rightValue = Integer.parseInt(rightPrimary.INT().getText());
                    int sum = leftValue + rightValue;

                    // Create a new PrimaryContext with the sum as an integer literal
                    ImperativeCompConstParser.PrimaryContext newPrimary = new ImperativeCompConstParser.PrimaryContext(ctx, ctx.invokingState);
                    TerminalNodeImpl newIntNode = new TerminalNodeImpl(new CommonToken(ImperativeCompConstParser.INT, Integer.toString(sum)));
                    newPrimary.addChild(newIntNode);

                    // Clear all children of FactorContext and add the new PrimaryContext directly
                    while (ctx.getChildCount() > 0) {
                        ctx.removeLastChild();
                    }
                    ctx.addAnyChild(newPrimary);

                    return null;
                }
            }
        }
        return visitChildren(ctx);
    }

    private boolean isIntOrReal(ImperativeCompConstParser.PrimaryContext primary) {
        return primary.INT() != null || primary.REAL() != null;
    }


//    @Override
//    public Object visitRelation(ImperativeCompConstParser.RelationContext ctx) {
//        System.out.println("visit relation");
//        System.out.println(ctx.getChildCount());
//        System.out.println(ctx.getChild(0).getText());
//        return visitChildren(ctx);
//    }

//    @Override
//    public Object visitFactor(ImperativeCompConstParser.FactorContext ctx) {
//        System.out.println("visit factor");
//        System.out.println(ctx.getChildCount());
//        System.out.println(ctx.getChild(0).getText());
//        return visitChildren(ctx);
//    }

//    @Override
//    public Object visitExpression(ImperativeCompConstParser.ExpressionContext ctx) {
//        System.out.println("visit expression");
//        return visitChildren(ctx);
//    }


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