package compiler;

import gen.ImperativeCompConstParser;

public class DeclarationCompileVisitor extends TypeCompileVisitor {
    @Override
    public Void visitVariable_declaration(ImperativeCompConstParser.Variable_declarationContext ctx) {
        String varName = ctx.IDENT().getText();
        String varType = ctx.type().getText();
        Integer idx = stackIndex++;

        variableTable.put(varName, new Helper(idx, varType));
        super.visitVariable_declaration(ctx);
        switch (varType) {
            case "integer" -> appendln("istore " + idx + " ; store integer variable");
            case "float" -> appendln("fstore " + idx + " ; store float variable");
            case "boolean" -> appendln("istore " + idx + " ; store boolean variable");
            default -> appendln("astore " + idx + " ; store reference variable");
        }
        return null;
    }
}
