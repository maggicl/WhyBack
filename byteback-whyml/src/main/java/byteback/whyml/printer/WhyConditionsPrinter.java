package byteback.whyml.printer;

import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
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
import java.util.stream.Stream;

public class WhyConditionsPrinter implements WhyCondition.Visitor {
	private final boolean isRecursive;
	private final boolean isSpec;

	private final List<Code> requiresList = new ArrayList<>();
	private final List<Code> ensuresList = new ArrayList<>();
	private final List<Code> returnsList = new ArrayList<>();
	private final List<Code> raisesList = new ArrayList<>();
	private final List<Code> decreasesList = new ArrayList<>();

	public WhyConditionsPrinter(boolean isRecursive, boolean isSpec) {
		this.isRecursive = isRecursive;
		this.isSpec = isSpec;
	}

	@Override
	public void visitRequires(WhyCondition.Requires r) {
		requiresList.add(r.value().toWhy().statement("requires { ", " }"));
	}

	@Override
	public void visitEnsures(WhyCondition.Ensures r) {
		final SExpr ensures = isSpec
				? r.value().toWhy()
		        : prefix(
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
		if (!isSpec) {
			// note: the `returns` condition in WhyML is a predicate that is guaranteed to hold on the return value
			// the meaning of @Returns in bb-lib simply denotes that no exceptions are returned when the condition holds.

			returnsList.add(r.when().toWhy().statement("ensures { ", " -> must_return result }"));
		}
	}

	@Override
	public void visitRaises(WhyCondition.Raises r) {
		if (isSpec) {
			throw new IllegalStateException("spec function is not allowed to have a raises condition: " + r);
		}

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

	public void print(Stream<WhyCondition> conditions) {
		conditions.forEach(this::visit);
	}

	public Code conditionStatements() {
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
