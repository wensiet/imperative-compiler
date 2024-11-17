package compiler;


import gen.ImperativeCompConstParser;

public class CompileVisitor extends DeclarationCompileVisitor {
    @Override
    public Void visitProgram(ImperativeCompConstParser.ProgramContext ctx) {
        appendln(".class public Program");
        appendln(".super java/lang/Object");

        appendln(".method public static main([Ljava/lang/String;)V");
        appendln(".limit stack 1000");
        appendln(".limit locals 1000");

        super.visitProgram(ctx);

        appendln("return");
        appendln(".end method");

        System.out.println("\nVars:");
        for (var snus : variableTable.values()) {
            System.out.println(snus);
        }

        return null;
    }
}
