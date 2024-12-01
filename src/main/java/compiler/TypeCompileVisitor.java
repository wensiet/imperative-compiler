package compiler;

import gen.ImperativeCompConstParser;

import java.util.HashMap;
import java.util.Map;

public class TypeCompileVisitor extends BodyCompileVisitor {
    Map<String, Integer> arrayTable = new HashMap<>();

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

        variableTable.put(varName, new Helper(idx, varType));

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

    @Override
    public Void visitArray_variable_declaration(ImperativeCompConstParser.Array_variable_declarationContext ctx) {
        String varName = ctx.IDENT().getText();
        String varType = ctx.type().getText();
        Integer idx = stackIndex++;

        variableTable.put(varName, new Helper(idx, varType));

        var arraySize = Integer.parseInt(ctx.type().array_type().expression().relation().get(0).simple().get(0).factor().get(0).summand().get(0).primary().getText()) - 1;
        visit(ctx.type().array_type());
        var expression = ctx.expressions();

        appendln("dup");
        appendln("astore " + idx);

        while (expression != null) {
            appendln("aload " + idx);
            appendln("ldc " + arraySize);
            visit(expression.expression());
            if (varType.contains("integer")) {
                appendln("iastore");
            } else if (varType.contains("real")) {
                appendln("fastore");
            } else if (varType.contains("boolean")) {
                appendln("bastore");
            }
            expression = expression.expressions();
            arraySize--;
        }
        return null;
    }

    @Override
    public Void visitArray_type(ImperativeCompConstParser.Array_typeContext ctx) {
        var type = ctx.type().getText();
        visitChildren(ctx);
        switch (type) {
            case "integer":
                appendln("newarray int");
                break;
            case "real":
                appendln("newarray float");
                break;
            case "boolean":
                appendln("newarray boolean");
                break;
            default:
                throw new IllegalArgumentException("Unsupported array type: " + type);
        }
        return null;
    }
}
