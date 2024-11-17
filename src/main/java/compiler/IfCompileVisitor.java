package compiler;

import gen.ImperativeCompConstParser;

public class IfCompileVisitor extends ExpressionCompileVisitor {
    @Override
    public Void visitIf_statement(ImperativeCompConstParser.If_statementContext ctx) {
        // Step 1: Evaluate the condition (expression)
        visit(ctx.expression());  // This pushes the result of the expression onto the stack

        // Step 2: Handle the 'then' part of the statement
        String elseLabel = generateLabel();  // Label for the else block (if it exists)
        String endLabel = generateLabel();   // Label for the end of the if statement

        // If the expression is false, jump to else or end
        appendln("ifeq " + elseLabel);  // Jump to else block if condition is false

        // Step 3: Execute the 'then' block (if condition is true)
        visit(ctx.body(0));  // Visit the first body (the 'then' body)

        // After the 'then' block, we jump to the end of the statement
        appendln("goto " + endLabel);  // Jump to the end of the if statement

        // Step 4: Handle the 'else' part (if it exists)
        appendln(elseLabel + ":");  // Mark the start of the else block

        if (ctx.ELSESYM() != null) {
            // There is an 'else' part, so we visit the second body (the 'else' body)
            visit(ctx.body(1));  // Visit the 'else' body
        }

        // Step 5: Mark the end of the if statement
        appendln(endLabel + ":");  // Mark the end of the if statement

        return null;
    }

}
