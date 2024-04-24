package byteback.whyml.printer;

import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.SExpr.infix;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import static byteback.whyml.printer.Statement.many;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.UnaryExpression;
import byteback.whyml.syntax.expr.binary.BinaryExpression;
import byteback.whyml.syntax.expr.binary.LogicConnector;
import byteback.whyml.syntax.function.WhyCondition;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class WhyConditionsPrinter implements WhyCondition.Visitor {
	private final List<Statement> requiresList = new ArrayList<>();
	private final List<Statement> ensuresList = new ArrayList<>();
	private final List<Expression> returnsList = new ArrayList<>();
	private final List<SExpr> raisesList = new ArrayList<>();

	@Override
	public void visitRequires(WhyCondition.Requires r) {
		requiresList.add(r.value().toWhy().statement("requires { ", " }"));
	}

	@Override
	public void visitEnsures(WhyCondition.Ensures r) {
		ensuresList.add(r.value().toWhy().statement("ensures { ", " }"));
	}

	@Override
	public void visitReturns(WhyCondition.Returns r) {
		// note: the `returns` condition in WhyML is a predicate that is guaranteed to hold on the return value
		// the meaning of @Returns in bb-lib simply denotes that no exceptions are returned when the condition holds.
		// We then build a disjunction of all return expressions should return false
		returnsList.add(r.when());
	}

	@Override
	public void visitRaises(WhyCondition.Raises r) {
		raisesList.add(
				infix("/\\",
						infix(":>",
								prefix("Class", terminal(Identifier.Special.EXCEPTION_PARAM.toString())),
								prefix("Class", terminal(r.getException() + ".class"))
						),
						r.getWhen().toWhy()
				)
		);
	}

	public void print(List<WhyCondition> conditions) {
		for (final WhyCondition c : conditions) {
			visit(c);
		}
	}

	public Statement conditionStatements() {
		final Optional<SExpr> returnsClause = returnsList.stream()
				.reduce((a, b) -> new BinaryExpression(LogicConnector.OR, a, b))
				.map(e -> new UnaryExpression(UnaryExpression.Operator.NOT, e).toWhy());

		final Optional<SExpr> raisesClause = raisesList.stream().reduce((a, b) -> infix("\\/", a, b));

		return many(
				many(requiresList.stream()),
				many(ensuresList.stream()),
				many(Stream.concat(returnsClause.stream(), raisesClause.stream())
						.reduce((a, b) -> infix("&&", a, b))
						.map(e -> e.statement(
								"raises { JException %s -> "
										.formatted(Identifier.Special.EXCEPTION_PARAM.toString()),
								" }"))
						.stream())
		);
	}
}
