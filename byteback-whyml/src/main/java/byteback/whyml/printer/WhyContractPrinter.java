package byteback.whyml.printer;

import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.InstanceOfExpression;
import byteback.whyml.syntax.expr.LocalExpression;
import byteback.whyml.syntax.expr.NullLiteral;
import byteback.whyml.syntax.expr.binary.BinaryExpression;
import byteback.whyml.syntax.expr.binary.Comparison;
import byteback.whyml.syntax.expr.binary.LogicConnector;
import byteback.whyml.syntax.function.WhyCondition;
import byteback.whyml.syntax.function.WhyFunction;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyReference;
import java.util.ArrayList;
import java.util.List;

public class WhyContractPrinter implements WhyCondition.Visitor {
	private final boolean isRecursive;
	private final WhyFunction function;

	private final List<Code> requiresList = new ArrayList<>();
	private final List<Code> ensuresList = new ArrayList<>();
	private final List<Code> returnsList = new ArrayList<>();
	private final List<Code> raisesList = new ArrayList<>();
	private final List<Code> decreasesList = new ArrayList<>();
	private final List<Code> readsList = new ArrayList<>();
	private final List<Code> writesList = new ArrayList<>();

	public WhyContractPrinter(boolean isRecursive, WhyFunction function) {
		this.isRecursive = isRecursive;
		this.function = function;
	}

	private static Expression caughtExceptionIsNull(boolean isNull) {
		return new BinaryExpression(
				new Comparison(
						WhyJVMType.PTR,
						isNull ? Comparison.Kind.EQ : Comparison.Kind.NE
				),
				new LocalExpression(WhyLocal.CAUGHT_EXCEPTION),
				NullLiteral.INSTANCE
		);
	}

	@Override
	public void visitRequires(WhyCondition.Requires r) {
		requiresList.add(r.value().toWhy().statement("requires { ", " }"));
	}

	@Override
	public void visitEnsures(WhyCondition.Ensures r) {
		ensuresList.add(
				new BinaryExpression(LogicConnector.IMPLIES, caughtExceptionIsNull(true), r.value())
						.toWhy()
						.statement("ensures { ", " }"));
	}

	@Override
	public void visitDecreases(WhyCondition.Decreases r) {
		decreasesList.add(r.value().toWhy().statement("variant { ", " }"));
	}

	@Override
	public void visitReturns(WhyCondition.Returns r) {
		if (function.contract().signature().declaration().isProgram()) {
			// note: the `returns` condition in WhyML is a predicate that is guaranteed to hold on the return value
			// the meaning of @Returns in bb-lib simply denotes that no exceptions are returned when the condition holds.

			returnsList.add(
					new BinaryExpression(
							LogicConnector.IMPLIES,
							r.when(),
							caughtExceptionIsNull(false)
					).toWhy().statement("ensures { ", " }")
			);
		}
	}

	@Override
	public void visitRaises(WhyCondition.Raises r) {
		if (function.contract().signature().declaration().isSpec()) {
			throw new IllegalStateException("spec function is not allowed to have a raises condition: " + r);
		}

		final Expression expr = new BinaryExpression(
				LogicConnector.IMPLIES,
				new InstanceOfExpression(
						new LocalExpression(Identifier.Special.CAUGHT_EXCEPTION, WhyJVMType.PTR),
						new WhyReference(r.getException())
				),
				r.getWhen()
		);

		raisesList.add(expr.toWhy().statement("ensures { ", " }"));
	}

	public void visit() {
		final WhyFunctionSignature sig = function.contract().signature();

		if (sig.declaration().isProgram()) {
			// if this is a program function, require that the caught exception variable is null before calling the method
			visit(new WhyCondition.Requires(caughtExceptionIsNull(true)));
		}

		for (final WhyLocal p : sig.params()) {
			p.condition().ifPresent(c -> visit(new WhyCondition.Requires(c)));
		}

		sig.resultParam().condition()
				.ifPresent(c -> visit(new WhyCondition.Ensures(c)));

		for (final WhyCondition c : function.contract().conditions()) {
			visit(c);
		}

		function.body().ifPresent(b -> {
			if (decreasesList.isEmpty() && isRecursive) {
				decreasesList.add(line("variant { 0 } (* no variant on method *)"));
			}

			for (final String w : b.sideEffects().reads()) {
				readsList.add(line("reads { %s }".formatted(w)));
			}

			for (final String w : b.sideEffects().writes()) {
				writesList.add(line("writes { %s }".formatted(w)));
			}
		});
	}

	public Code conditionStatements() {
		return many(
				many(requiresList.stream()),
				many(ensuresList.stream()),
				many(returnsList.stream()),
				many(raisesList.stream()),
				many(decreasesList.stream()),
				many(readsList.stream()),
				many(writesList.stream())
		);
	}
}
