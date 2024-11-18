package compiler;

import gen.ImperativeCompConstParser;

import java.util.ArrayList;
import java.util.List;

public class BodyCompileVisitor extends RoutineCompileVisitor {
    @Override
    public Void visitAssignment(ImperativeCompConstParser.AssignmentContext ctx) {
        // Flatten the access chain of the left-hand side (modifiable_primary)
        List<String> accessChain = new ArrayList<>();
        ImperativeCompConstParser.Modifiable_primaryContext currentCtx = ctx.modifiable_primary();

        while (currentCtx != null) {
            if (currentCtx.IDENT() != null) {
                // Add the identifier to the access chain
                accessChain.addFirst(currentCtx.IDENT().getText());
            }
            currentCtx = currentCtx.modifiable_primary();
        }

        // Resolve the base variable
        String baseVarName = accessChain.removeFirst();
        Helper baseVarHelper = variableTable.get(baseVarName);
        if (baseVarHelper == null) {
            throw new RuntimeException("Variable not declared: " + baseVarName);
        }

        String currentType = baseVarHelper.type;

        // Load the base variable (reference) onto the stack
        int baseVarIdx = baseVarHelper.idx;
        appendln("aload " + baseVarIdx + " ; load reference variable");

        // Iteratively resolve each field in the chain, except for the last field
        for (int i = 0; i < accessChain.size() - 1; i++) {
            String fieldName = accessChain.get(i);

            // Ensure the current type is a record
            UserDefinedType recordType = userDefinedTypes.get(currentType);
            if (recordType == null) {
                throw new RuntimeException("Type '" + currentType + "' is not a user-defined record");
            }

            // Get the field type
            String fieldType = recordType.fields.get(fieldName);
            if (fieldType == null) {
                throw new RuntimeException("Field '" + fieldName + "' is not defined in record type '" + currentType + "'");
            }

            // Emit code to access the field
            appendln("getfield " + currentType + "/" + fieldName + " " + mapType(fieldType));

            // Update the current type
            currentType = fieldType;
        }

        // Evaluate the right-hand side expression or routine call (push value onto the stack)
        visit(ctx.expression() != null ? ctx.expression() : ctx.routine_call());

        // Handle the final field assignment
        String finalFieldName = accessChain.getLast();

        // Ensure the current type is a record
        UserDefinedType recordType = userDefinedTypes.get(currentType);
        if (recordType == null) {
            throw new RuntimeException("Type '" + currentType + "' is not a user-defined record");
        }

        // Get the field type
        String finalFieldType = recordType.fields.get(finalFieldName);
        if (finalFieldType == null) {
            throw new RuntimeException("Field '" + finalFieldName + "' is not defined in record type '" + currentType + "'");
        }

        // Emit code to set the field value (reference must already be on the stack)
        appendln("putfield " + currentType + "/" + finalFieldName + " " + mapType(finalFieldType));

        return null;
    }

}
