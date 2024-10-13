// Generated from /Users/wensiet/IdeaProjects/imperative-compiler/src/main/antlr4/ImperativeCompConst.g4 by ANTLR 4.13.2
package gen;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ImperativeCompConstParser}.
 */
public interface ImperativeCompConstListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#input}.
	 * @param ctx the parse tree
	 */
	void enterInput(ImperativeCompConstParser.InputContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#input}.
	 * @param ctx the parse tree
	 */
	void exitInput(ImperativeCompConstParser.InputContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(ImperativeCompConstParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(ImperativeCompConstParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#simple_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSimple_declaration(ImperativeCompConstParser.Simple_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#simple_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSimple_declaration(ImperativeCompConstParser.Simple_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#variable_declaration}.
	 * @param ctx the parse tree
	 */
	void enterVariable_declaration(ImperativeCompConstParser.Variable_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#variable_declaration}.
	 * @param ctx the parse tree
	 */
	void exitVariable_declaration(ImperativeCompConstParser.Variable_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#array_variable_declaration}.
	 * @param ctx the parse tree
	 */
	void enterArray_variable_declaration(ImperativeCompConstParser.Array_variable_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#array_variable_declaration}.
	 * @param ctx the parse tree
	 */
	void exitArray_variable_declaration(ImperativeCompConstParser.Array_variable_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#expressions}.
	 * @param ctx the parse tree
	 */
	void enterExpressions(ImperativeCompConstParser.ExpressionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#expressions}.
	 * @param ctx the parse tree
	 */
	void exitExpressions(ImperativeCompConstParser.ExpressionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#record_variable_declaration}.
	 * @param ctx the parse tree
	 */
	void enterRecord_variable_declaration(ImperativeCompConstParser.Record_variable_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#record_variable_declaration}.
	 * @param ctx the parse tree
	 */
	void exitRecord_variable_declaration(ImperativeCompConstParser.Record_variable_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#record_field_assignments}.
	 * @param ctx the parse tree
	 */
	void enterRecord_field_assignments(ImperativeCompConstParser.Record_field_assignmentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#record_field_assignments}.
	 * @param ctx the parse tree
	 */
	void exitRecord_field_assignments(ImperativeCompConstParser.Record_field_assignmentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#type_declaration}.
	 * @param ctx the parse tree
	 */
	void enterType_declaration(ImperativeCompConstParser.Type_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#type_declaration}.
	 * @param ctx the parse tree
	 */
	void exitType_declaration(ImperativeCompConstParser.Type_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#routine_declaration}.
	 * @param ctx the parse tree
	 */
	void enterRoutine_declaration(ImperativeCompConstParser.Routine_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#routine_declaration}.
	 * @param ctx the parse tree
	 */
	void exitRoutine_declaration(ImperativeCompConstParser.Routine_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#parameter_declarations}.
	 * @param ctx the parse tree
	 */
	void enterParameter_declarations(ImperativeCompConstParser.Parameter_declarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#parameter_declarations}.
	 * @param ctx the parse tree
	 */
	void exitParameter_declarations(ImperativeCompConstParser.Parameter_declarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(ImperativeCompConstParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(ImperativeCompConstParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#primitive_type}.
	 * @param ctx the parse tree
	 */
	void enterPrimitive_type(ImperativeCompConstParser.Primitive_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#primitive_type}.
	 * @param ctx the parse tree
	 */
	void exitPrimitive_type(ImperativeCompConstParser.Primitive_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#record_type}.
	 * @param ctx the parse tree
	 */
	void enterRecord_type(ImperativeCompConstParser.Record_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#record_type}.
	 * @param ctx the parse tree
	 */
	void exitRecord_type(ImperativeCompConstParser.Record_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#record_variable_declarations}.
	 * @param ctx the parse tree
	 */
	void enterRecord_variable_declarations(ImperativeCompConstParser.Record_variable_declarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#record_variable_declarations}.
	 * @param ctx the parse tree
	 */
	void exitRecord_variable_declarations(ImperativeCompConstParser.Record_variable_declarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#array_type}.
	 * @param ctx the parse tree
	 */
	void enterArray_type(ImperativeCompConstParser.Array_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#array_type}.
	 * @param ctx the parse tree
	 */
	void exitArray_type(ImperativeCompConstParser.Array_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(ImperativeCompConstParser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(ImperativeCompConstParser.BodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#return_expression}.
	 * @param ctx the parse tree
	 */
	void enterReturn_expression(ImperativeCompConstParser.Return_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#return_expression}.
	 * @param ctx the parse tree
	 */
	void exitReturn_expression(ImperativeCompConstParser.Return_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(ImperativeCompConstParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(ImperativeCompConstParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(ImperativeCompConstParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(ImperativeCompConstParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#routine_call}.
	 * @param ctx the parse tree
	 */
	void enterRoutine_call(ImperativeCompConstParser.Routine_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#routine_call}.
	 * @param ctx the parse tree
	 */
	void exitRoutine_call(ImperativeCompConstParser.Routine_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#routine_call_arguments}.
	 * @param ctx the parse tree
	 */
	void enterRoutine_call_arguments(ImperativeCompConstParser.Routine_call_argumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#routine_call_arguments}.
	 * @param ctx the parse tree
	 */
	void exitRoutine_call_arguments(ImperativeCompConstParser.Routine_call_argumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#while_loop}.
	 * @param ctx the parse tree
	 */
	void enterWhile_loop(ImperativeCompConstParser.While_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#while_loop}.
	 * @param ctx the parse tree
	 */
	void exitWhile_loop(ImperativeCompConstParser.While_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#for_loop}.
	 * @param ctx the parse tree
	 */
	void enterFor_loop(ImperativeCompConstParser.For_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#for_loop}.
	 * @param ctx the parse tree
	 */
	void exitFor_loop(ImperativeCompConstParser.For_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#foreach_loop}.
	 * @param ctx the parse tree
	 */
	void enterForeach_loop(ImperativeCompConstParser.Foreach_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#foreach_loop}.
	 * @param ctx the parse tree
	 */
	void exitForeach_loop(ImperativeCompConstParser.Foreach_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(ImperativeCompConstParser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(ImperativeCompConstParser.RangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void enterIf_statement(ImperativeCompConstParser.If_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#if_statement}.
	 * @param ctx the parse tree
	 */
	void exitIf_statement(ImperativeCompConstParser.If_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ImperativeCompConstParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ImperativeCompConstParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelation(ImperativeCompConstParser.RelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelation(ImperativeCompConstParser.RelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#simple}.
	 * @param ctx the parse tree
	 */
	void enterSimple(ImperativeCompConstParser.SimpleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#simple}.
	 * @param ctx the parse tree
	 */
	void exitSimple(ImperativeCompConstParser.SimpleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(ImperativeCompConstParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(ImperativeCompConstParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#summand}.
	 * @param ctx the parse tree
	 */
	void enterSummand(ImperativeCompConstParser.SummandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#summand}.
	 * @param ctx the parse tree
	 */
	void exitSummand(ImperativeCompConstParser.SummandContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(ImperativeCompConstParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(ImperativeCompConstParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImperativeCompConstParser#modifiable_primary}.
	 * @param ctx the parse tree
	 */
	void enterModifiable_primary(ImperativeCompConstParser.Modifiable_primaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImperativeCompConstParser#modifiable_primary}.
	 * @param ctx the parse tree
	 */
	void exitModifiable_primary(ImperativeCompConstParser.Modifiable_primaryContext ctx);
}