package semantic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gen.ImperativeCompConstBaseVisitor;
import gen.ImperativeCompConstParser;

public class SemanticAnalyzerVisitor extends ImperativeCompConstBaseVisitor<Void> {

    private final Map<String, String> symbolTable = new HashMap<>();
    private boolean inRoutine = false;

    private void setNewObject(String varName) {
        if (symbolTable.containsKey(varName)) {
            throw new RuntimeException("Error: '" + varName + "' is already declared.");
        } else {
            symbolTable.put(varName, "1");
        }
    }

    @Override
    public Void visitRoutine_declaration(ImperativeCompConstParser.Routine_declarationContext ctx) {
        String routineName = ctx.IDENT().getText();
        setNewObject(routineName);
        inRoutine = true;
        saveRoutineParameters(ctx.parameter_declarations());
        visitChildren(ctx);
        inRoutine = false;
        removeRoutineParameters(ctx.parameter_declarations());
        return null;
    }

    private void saveRoutineParameters(ImperativeCompConstParser.Parameter_declarationsContext paramDeclCtx) {
        while (paramDeclCtx != null) {
            String paramName = paramDeclCtx.IDENT().getText();
            setNewObject(paramName);
            paramDeclCtx = paramDeclCtx.parameter_declarations();
        }
    }

    private void removeRoutineParameters(ImperativeCompConstParser.Parameter_declarationsContext paramDeclCtx) {
        while (paramDeclCtx != null) {
            String paramName = paramDeclCtx.IDENT().getText();
            symbolTable.remove(paramName);
            paramDeclCtx = paramDeclCtx.parameter_declarations();
        }
    }

    @Override
    public Void visitVariable_declaration(ImperativeCompConstParser.Variable_declarationContext ctx) {
        String varName = ctx.IDENT().getText();
        setNewObject(varName);
        return visitChildren(ctx);
    }


    @Override
    public Void visitModifiable_primary(ImperativeCompConstParser.Modifiable_primaryContext ctx) {
        String varName = ctx.IDENT().getText();
        if (!symbolTable.containsKey(varName)) {
            throw new RuntimeException("Error: Variable '" + varName + "' is used before being declared.");
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitFor_loop(ImperativeCompConstParser.For_loopContext ctx) {
        String loopVarName = ctx.IDENT().getText();
        setNewObject(loopVarName);
        visitChildren(ctx);
        symbolTable.remove(loopVarName);
        return null;
    }

    @Override
    public Void visitRoutine_call(ImperativeCompConstParser.Routine_callContext ctx) {
        String routineName = ctx.IDENT().getText();
        if ("print".equals(routineName)) {
            return visitChildren(ctx);
        }
        if (!symbolTable.containsKey(routineName)) {
            throw new RuntimeException("Error: Routine '" + routineName + "' is called before being declared.");
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitReturn_expression(ImperativeCompConstParser.Return_expressionContext ctx) {
        if (!inRoutine) {
            throw new RuntimeException("Error: 'return' used outside of a routine.");
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitRecord_variable_declaration(ImperativeCompConstParser.Record_variable_declarationContext ctx) {
        String recordName = ctx.IDENT().getText();
        setNewObject(recordName);
        Map<String, String> fieldTable = new HashMap<>();

        return visitChildren(ctx);
    }

    @Override
    public Void visitRecord_field_assignments(ImperativeCompConstParser.Record_field_assignmentsContext ctx) {
        return visitChildren(ctx);
    }


    @Override
    public Void visitRecord_type(ImperativeCompConstParser.Record_typeContext ctx) {
        String varName = ctx.record_variable_declarations().IDENT().getText();
        setNewObject(varName);
        return visitChildren(ctx);
    }

    @Override
    public Void visitRecord_variable_declarations(ImperativeCompConstParser.Record_variable_declarationsContext ctx) {
        Set<String> fieldNames = new HashSet<>();

        ImperativeCompConstParser.Record_variable_declarationsContext fieldCtx = ctx;
        while (fieldCtx != null) {
            String fieldName = fieldCtx.IDENT().getText();

            if (!fieldNames.add(fieldName)) {
                throw new RuntimeException("Error: Field '" + fieldName + "' is already declared in the record.");
            }

            fieldCtx = fieldCtx.record_variable_declarations();
        }

        return visitChildren(ctx);
    }


    @Override
    public Void visitType_declaration(ImperativeCompConstParser.Type_declarationContext ctx) {
        String varName = ctx.IDENT().getText();
        setNewObject(varName);
        return visitChildren(ctx);
    }
}
