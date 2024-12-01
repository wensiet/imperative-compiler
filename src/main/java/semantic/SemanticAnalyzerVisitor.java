package semantic;

import java.util.*;

import gen.ImperativeCompConstBaseVisitor;
import gen.ImperativeCompConstParser;
import org.antlr.v4.runtime.tree.ParseTree;

import static java.lang.System.exit;

public class SemanticAnalyzerVisitor extends ImperativeCompConstBaseVisitor<Void> {

    private final Map<String, String> symbolTable = new HashMap<>();
    private final ArrayList<Record> records = new ArrayList<>();
    private boolean inRoutine = false;

    private void setNewObject(String varName) {
        if (symbolTable.containsKey(varName)) {
            throw new RuntimeException("Error: '" + varName + "' is already declared.");
        } else {
            symbolTable.put(varName, "1");
        }
    }

    @Override
    public Void visit(ParseTree tree) {
        try {
            return super.visit(tree);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            exit(1);
        }
        return null;
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

    private boolean checkExistence(String varName) {
        var recordPresent = false;
        for (Record record : records) {
            for (String var : record.fields) {
                if (var.equals(varName)) {
                    recordPresent = true;
                    break;
                }
            }
        }
        return symbolTable.containsKey(varName) | recordPresent;
    }

    @Override
    public Void visitVariable_declaration(ImperativeCompConstParser.Variable_declarationContext ctx) {
        String varName = ctx.IDENT().getText();
        setNewObject(varName);
        return visitChildren(ctx);
    }


    @Override
    public Void visitModifiable_primary(ImperativeCompConstParser.Modifiable_primaryContext ctx) {
        if (ctx.IDENT() != null) {
            String varName = ctx.IDENT().getText();
            if (!checkExistence(varName)) {
                throw new RuntimeException("Error: Variable '" + varName + "' is used before being declared.");
            }
        } else {
            String varName = ctx.modifiable_primary().IDENT().getText();
            if (!checkExistence(varName)) {
                throw new RuntimeException("Error: Variable '" + varName + "' is used before being declared.");
            }
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
        if (routineName.equals("print")) {
            return visitChildren(ctx);
        }
        if (!checkExistence(routineName)) {
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
    public Void visitType_declaration(ImperativeCompConstParser.Type_declarationContext ctx) {
        if (ctx.type().record_type() != null) {
            var recordName = ctx.IDENT().getText();
            Set<String> fieldNames = new HashSet<>();
            var variableDeclarationsContext = ctx.type().record_type().record_variable_declarations();
            while (variableDeclarationsContext != null) {
                String fieldName = variableDeclarationsContext.IDENT().getText();
                if (!fieldNames.add(fieldName)) {
                    throw new RuntimeException("Error: Field '" + fieldName + "' is already declared in the record.");
                }
                variableDeclarationsContext = variableDeclarationsContext.record_variable_declarations();
            }
            records.add(new Record(recordName, fieldNames));
        } else {
            String varName = ctx.IDENT().getText();
            setNewObject(varName);
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitArray_variable_declaration(ImperativeCompConstParser.Array_variable_declarationContext ctx) {
        String varName = ctx.IDENT().getText();
        setNewObject(varName);
        return visitChildren(ctx);
    }
}
