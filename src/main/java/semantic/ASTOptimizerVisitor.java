package semantic;

import gen.ImperativeCompConstBaseVisitor;
import gen.ImperativeCompConstParser;
import org.antlr.v4.runtime.tree.ParseTree;

public class ASTOptimizerVisitor extends ImperativeCompConstBaseVisitor<Void> {
    @Override
    public Void visitIf_statement(ImperativeCompConstParser.If_statementContext ctx) {
        if (isConstantExpression(ctx.expression())) {
            boolean conditionValue = evaluateCondition(ctx.expression());
            ParseTree ifTrue = ctx.body(0);
            ParseTree ifFalse = ctx.body(1);
            while (ctx.getChildCount() > 0) {
                ctx.removeLastChild();
            }
            if (conditionValue) {
                ctx.addAnyChild(ifTrue);
            } else if (ifFalse != null) {
                ctx.addAnyChild(ifFalse);
            }
        }
        return visitChildren(ctx);
    }

    private boolean isConstantExpression(ImperativeCompConstParser.ExpressionContext expression) {
        ImperativeCompConstParser.PrimaryContext primary = expression.relation(0).simple(0).factor(0).summand(0).primary();
        return primary.FALSE() != null || primary.TRUE() != null;
    }

    private boolean evaluateCondition(ImperativeCompConstParser.ExpressionContext expression) {
        ImperativeCompConstParser.PrimaryContext primary = expression.relation(0).simple(0).factor(0).summand(0).primary();
        return primary.TRUE() != null;
    }
}
