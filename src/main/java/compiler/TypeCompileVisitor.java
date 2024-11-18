package compiler;

import gen.ImperativeCompConstParser;

public class TypeCompileVisitor extends BodyCompileVisitor {
    @Override
    public Void visitType_declaration(ImperativeCompConstParser.Type_declarationContext ctx) {
        if (ctx.type().record_type() != null) {
            String recordName = ctx.IDENT().getText();
            RecordCompileVisitor visitor = new RecordCompileVisitor(recordName);
            visitor.visit(ctx.type().record_type());
            addRecord(new Record(recordName, visitor.getJasminCode()));
            var type = visitor.getUserDefinedType();
            userDefinedTypes.put(type.name, type);
        } else if (ctx.type().array_type() != null) {
            visit(ctx.type().array_type());
        } else if (ctx.type().primitive_type() != null) {
            visit(ctx.type().primitive_type());
        } else if (ctx.IDENT() != null) {
            appendln("; Using user-defined type: " + ctx.IDENT().getText());
        }
        return null;
    }

    @Override
    public Void visitRecord_variable_declaration(ImperativeCompConstParser.Record_variable_declarationContext ctx) {
        String varName = ctx.IDENT().getText();
        String varType = ctx.type().getText();
        Integer idx = stackIndex++;

        // Store the record variable in the variable table
        variableTable.put(varName, new Helper(idx, varType));

        // Create a new record instance
        appendln("new " + varType);
        appendln("dup");
        appendln("invokespecial " + varType + "/<init>()V");
        appendln("astore " + idx + " ; store record reference");

        var fieldAss = ctx.record_field_assignments();

        while (fieldAss != null) {
            appendln("aload " + idx);
            String fieldName = fieldAss.IDENT().getText();
            UserDefinedType type = userDefinedTypes.get(varType);
            if (type == null) {
                throw new IllegalStateException("Type " + varType + " not found");
            }
            String primType = type.fields.get(fieldName);
            if (primType == null) {
                throw new IllegalStateException("PrimitiveType " + fieldName + " not found");
            }
            visit(fieldAss.expression());
            appendln("putfield " + varType + "/" + fieldName + " " + mapType(primType));
            fieldAss = fieldAss.record_field_assignments();
        }

        return null;
    }
}
