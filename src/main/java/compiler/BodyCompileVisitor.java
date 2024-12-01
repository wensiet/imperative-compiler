package compiler;

import gen.ImperativeCompConstParser;

import java.util.ArrayList;
import java.util.List;

public class BodyCompileVisitor extends RoutineCompileVisitor {
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
            appendln("aload " + baseVarIdx + " ; load reference variable");

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

                appendln("getfield " + currentType + "/" + fieldName + " " + mapType(fieldType));

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

            appendln("putfield " + currentType + "/" + finalFieldName + " " + mapType(finalFieldType));

            return null;

        } else {
            visit(ctx.expression() != null ? ctx.expression() : ctx.routine_call());
            String varName = ctx.modifiable_primary().IDENT().getText();
            Helper varHelper = variableTable.get(varName);

            switch (varHelper.type) {
                case "integer" -> appendln("istore " + varHelper.idx + " ; store integer variable");
                case "real" -> appendln("fstore " + varHelper.idx + " ; store float variable");
                case "boolean" -> appendln("istore " + varHelper.idx + " ; store boolean variable");
                default -> appendln("astore " + varHelper.idx + " ; store reference variable");
            }
            return null;

        }
    }

}
