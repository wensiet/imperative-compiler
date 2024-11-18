package compiler;

import gen.ImperativeCompConstParser;


public class RecordCompileVisitor extends CompileVisitor {
    private final UserDefinedType userDefinedType;

    public RecordCompileVisitor(String className) {
        this.className = className;
        this.userDefinedType = new UserDefinedType(className);
    }

    public UserDefinedType getUserDefinedType() {
        return userDefinedType;
    }

    @Override
    public Void visitRecord_type(ImperativeCompConstParser.Record_typeContext ctx) {
        appendln(".class public " + className);
        appendln(".super java/lang/Object");

        // Define fields from the record_variable_declarations
        visit(ctx.record_variable_declarations());

        // Create default constructor
        appendln(".method public <init>()V");
        appendln(".limit stack 100");
        appendln(".limit locals 100");
        appendln("aload_0");
        appendln("invokespecial java/lang/Object/<init>()V");
        appendln("return");
        appendln(".end method");
        return null;
    }

    @Override
    public Void visitRecord_variable_declarations(ImperativeCompConstParser.Record_variable_declarationsContext ctx) {
        if (ctx != null) {
            String fieldName = ctx.IDENT().getText();
            String fieldType = ctx.type().getText();

            // Map type to Jasmin types
            appendln(".field public " + fieldName + " " + mapType(fieldType));
            userDefinedType.fields.put(fieldName, fieldType);

            if (ctx.record_variable_declarations() != null) {
                visit(ctx.record_variable_declarations());
            }
        }
        return null;
    }

}
