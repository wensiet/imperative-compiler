// Generated from /Users/wensiet/IdeaProjects/imperative-compiler/src/main/antlr4/ImperativeCompConst.g4 by ANTLR 4.13.2
package gen;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ImperativeCompConstParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ImperativeCompConstVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#input}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInput(ImperativeCompConstParser.InputContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(ImperativeCompConstParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#simple_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_declaration(ImperativeCompConstParser.Simple_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#variable_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_declaration(ImperativeCompConstParser.Variable_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#array_variable_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_variable_declaration(ImperativeCompConstParser.Array_variable_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#expressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressions(ImperativeCompConstParser.ExpressionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#record_variable_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecord_variable_declaration(ImperativeCompConstParser.Record_variable_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#record_field_assignments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecord_field_assignments(ImperativeCompConstParser.Record_field_assignmentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#type_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_declaration(ImperativeCompConstParser.Type_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#routine_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutine_declaration(ImperativeCompConstParser.Routine_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#parameter_declarations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_declarations(ImperativeCompConstParser.Parameter_declarationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(ImperativeCompConstParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#primitive_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitive_type(ImperativeCompConstParser.Primitive_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#record_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecord_type(ImperativeCompConstParser.Record_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#record_variable_declarations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecord_variable_declarations(ImperativeCompConstParser.Record_variable_declarationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#array_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_type(ImperativeCompConstParser.Array_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(ImperativeCompConstParser.BodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#return_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_expression(ImperativeCompConstParser.Return_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(ImperativeCompConstParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(ImperativeCompConstParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#routine_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutine_call(ImperativeCompConstParser.Routine_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#routine_call_arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutine_call_arguments(ImperativeCompConstParser.Routine_call_argumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#while_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_loop(ImperativeCompConstParser.While_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#for_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_loop(ImperativeCompConstParser.For_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#foreach_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeach_loop(ImperativeCompConstParser.Foreach_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange(ImperativeCompConstParser.RangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#if_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_statement(ImperativeCompConstParser.If_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(ImperativeCompConstParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#relation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation(ImperativeCompConstParser.RelationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#simple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple(ImperativeCompConstParser.SimpleContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(ImperativeCompConstParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#summand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSummand(ImperativeCompConstParser.SummandContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(ImperativeCompConstParser.PrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link ImperativeCompConstParser#modifiable_primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifiable_primary(ImperativeCompConstParser.Modifiable_primaryContext ctx);
}