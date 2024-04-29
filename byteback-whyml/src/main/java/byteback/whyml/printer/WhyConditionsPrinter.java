package byteback.whyml.printer;

import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import static byteback.whyml.printer.Statement.line;
import static byteback.whyml.printer.Statement.many;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.InstanceOfExpression;
import byteback.whyml.syntax.expr.LocalVariableExpression;
import byteback.whyml.syntax.expr.binary.BinaryExpression;
import byteback.whyml.syntax.expr.binary.LogicConnector;
import byteback.whyml.syntax.function.WhyCondition;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyReference;
import java.util.ArrayList;
import java.util.List;

public class WhyConditionsPrinter implements WhyCondition.Visitor {
	private final boolean isRecursive;

	private final List<Statement> requiresList = new ArrayList<>();
	private final List<Statement> ensuresList = new ArrayList<>();
	private final List<Statement> returnsList = new ArrayList<>();
	private final List<Statement> raisesList = new ArrayList<>();
	private final List<Statement> decreasesList = new ArrayList<>();

	public WhyConditionsPrinter(boolean isRecursive) {
		this.isRecursive = isRecursive;
	}

	@Override
	public void visitRequires(WhyCondition.Requires r) {
		requiresList.add(r.value().toWhy().statement("requires { ", " }"));
	}

	@Override
	public void visitEnsures(WhyCondition.Ensures r) {
		final SExpr ensures = prefix(
				"when_returns",
				terminal(Identifier.Special.RESULT),
				prefix(
						"fun",
						terminal("(%s)".formatted(Identifier.Special.RESULT_VAR)),
						terminal("->"),
						r.value().toWhy()
				)
		);

		ensuresList.add(ensures.statement("ensures { ", " }"));
	}

	@Override
	public void visitDecreases(WhyCondition.Decreases r) {
		decreasesList.add(r.value().toWhy().statement("variant { ", " }"));
	}

	@Override
	public void visitReturns(WhyCondition.Returns r) {
		// note: the `returns` condition in WhyML is a predicate that is guaranteed to hold on the return value
		// the meaning of @Returns in bb-lib simply denotes that no exceptions are returned when the condition holds.
		returnsList.add(r.when().toWhy().statement("ensures { ", " -> must_return result }"));
	}

	@Override
	public void visitRaises(WhyCondition.Raises r) {
		final Expression expr = new BinaryExpression(
				LogicConnector.AND,
				new InstanceOfExpression(
						new LocalVariableExpression(Identifier.Special.EXCEPTION_VAR, WhyJVMType.PTR),
						new WhyReference(r.getException())
				),
				r.getWhen()
		);

		final SExpr ensures = prefix(
				"when_throws",
				terminal(Identifier.Special.RESULT),
				prefix(
						"fun",
						terminal("(%s: Ptr.t)".formatted(Identifier.Special.EXCEPTION_VAR)),
						terminal("->"),
						expr.toWhy()
				)
		);

		raisesList.add(ensures.statement("ensures { ", " }"));
	}

	public void print(List<WhyCondition> conditions) {
		for (final WhyCondition c : conditions) {
			visit(c);
		}
	}

	public Statement conditionStatements() {
		return many(
				many(requiresList.stream()),
				many(ensuresList.stream()),
				decreasesList.isEmpty() && isRecursive
						? line("variant { 0 }")
						: many(decreasesList.stream()),
				many(returnsList.stream()),
				many(raisesList.stream())
		);
	}
}
