package compiler;


import gen.ImperativeCompConstParser;

public class CompileVisitor extends DeclarationCompileVisitor {
    protected String className = "Program";

    @Override
    public Void visitProgram(ImperativeCompConstParser.ProgramContext ctx) {
        appendln(".class public " + className);
        appendln(".super java/lang/Object");

        appendln(".method public static main([Ljava/lang/String;)V");
        appendln(".limit stack 1000");
        appendln(".limit locals 1000");

        super.visitProgram(ctx);

        appendln("return");
        appendln(".end method");

        for (var routine : routines.values()) {
            appendln(routine.body);
        }

        return null;
    }
}
