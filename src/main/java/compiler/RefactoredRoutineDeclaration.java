package compiler;

import gen.ImperativeCompConstParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RefactoredRoutineDeclaration extends CompileVisitor {
    Boolean nestedRoutineLock = false;
    List<Argument> args = new ArrayList<>();
    Routine resultingRoutine;

    public RefactoredRoutineDeclaration(Map<String, UserDefinedType> userDefinedTypes, int labelCounter) {
        this.userDefinedTypes = userDefinedTypes;
        this.labelCounter += labelCounter;
    }

    @Override
    public Void visitRoutine_declaration(ImperativeCompConstParser.Routine_declarationContext ctx) {
        if (nestedRoutineLock) {
            throw new RuntimeException("Cannot declare nested routine");
        }
        nestedRoutineLock = true;
        stackIndex--;

        String routineName = ctx.IDENT().getText();
        String returnType;
        if (ctx.type() != null) {
            returnType = mapType(ctx.type().getText());
        } else {
            returnType = "V";
        }
        resultingRoutine = new Routine(routineName, returnType);
        this.append(".method public static ");
        StringBuilder signature = new StringBuilder(routineName);

        signature.append("(");
        visitParameter_declarations(ctx.parameter_declarations());
        for (Argument arg : args.reversed()) {
            signature.append(mapType(arg.type));
        }
        resultingRoutine.args = args.reversed();
        signature.append(")");

        signature.append(returnType);

        resultingRoutine.signature = signature.toString();

        if (resultingRoutine.signature.contains("array")) {
            if (resultingRoutine.signature.contains("integer") | resultingRoutine.signature.contains("boolean")) {
                resultingRoutine.signature = resultingRoutine.signature.replaceAll("L.*?integer;", "[I");
            } else if (resultingRoutine.signature.contains("real")) {
                resultingRoutine.signature = resultingRoutine.signature.replaceAll("L.*?real;", "[F");
            }
        }

        appendln(resultingRoutine.signature);

        appendln(".limit stack 100");
        appendln(".limit locals 100");

        visit(ctx.body());

        appendln("return");
        appendln(".end method");

        resultingRoutine.body = new StringBuilder(getJasminCode());
        return null;
    }

    @Override
    public Void visitParameter_declarations(ImperativeCompConstParser.Parameter_declarationsContext ctx) {
        if (ctx.parameter_declarations() != null) visit(ctx.parameter_declarations());
        String varName = ctx.IDENT().getText();
        String varType = ctx.type().getText();
        Integer idx = stackIndex++;
        variableTable.put(varName, new Helper(idx, varType));
        args.add(new Argument(varName, varType));
        return null;
    }

    @Override
    public Void visitReturn_expression(ImperativeCompConstParser.Return_expressionContext ctx) {
        if (resultingRoutine.returnType.equals("V")) {
            throw new IllegalStateException("Function should not return anything but return statement found");
        }
        // TODO: check that inferred type of expression is compatible
        visit(ctx.expression());
        String returnCmd = switch (resultingRoutine.returnType) {
            case "I", "Z" -> "ireturn";
            case "F" -> "freturn";
            default -> "areturn";
        };
        appendln(returnCmd);
        return null;
    }
}
