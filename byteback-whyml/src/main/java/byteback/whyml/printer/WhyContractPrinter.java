package byteback.whyml.printer;

import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.InstanceOfExpression;
import byteback.whyml.syntax.expr.binary.BinaryExpression;
import byteback.whyml.syntax.expr.binary.LogicConnector;
import byteback.whyml.syntax.function.WhyCondition;
import byteback.whyml.syntax.function.WhyFunction;
import byteback.whyml.syntax.function.WhyFunctionBody;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.syntax.function.WhySideEffects;
import byteback.whyml.syntax.type.WhyReference;
import byteback.whyml.vimp.WhyResolver;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class WhyContractPrinter implements WhyCondition.Visitor {
	private final boolean isRecursive;
	private final WhyFunction function;
	private final WhyResolver resolver;

	private final List<Code> requiresList = new ArrayList<>();
	private final List<Code> ensuresList = new ArrayList<>();
	private final List<Code> returnsList = new ArrayList<>();
	private final List<Code> raisesList = new ArrayList<>();
	private final List<Code> decreasesList = new ArrayList<>();
	private final List<Code> readsList = new ArrayList<>();
	private final List<Code> writesList = new ArrayList<>();

	public WhyContractPrinter(boolean isRecursive, WhyFunction function, WhyResolver resolver) {
		this.isRecursive = isRecursive;
		this.function = function;
		this.resolver = resolver;
	}

	@Override
	public void visitRequires(WhyCondition.Requires r) {
		requiresList.add(r.value().getExpression().toWhy(true).statement("requires { ", " }"));
	}

	@Override
	public void visitEnsures(WhyCondition.Ensures r) {
		final Expression condition;
		condition = function.contract().signature().declaration().isProgram() && !r.hasExceptionParam()
				? new BinaryExpression(
						LogicConnector.IMPLIES,
						WhyLocal.CAUGHT_EXCEPTION.isNullExpression(),
						r.value().getExpression()
				)
				: r.value().getExpression();

		ensuresList.add(condition.toWhy(true).statement("ensures { ", " }"));
	}

	@Override
	public void visitDecreases(WhyCondition.Decreases r) {
		decreasesList.add(r.value().getExpression().toWhy(true).statement("variant { ", " }"));
	}

	@Override
	public void visitReturns(WhyCondition.Returns r) {
		if (function.contract().signature().declaration().isProgram()) {
			// note: the `returns` condition in WhyML is a predicate that is guaranteed to hold on the return value
			// the meaning of @Returns in bb-lib simply denotes that no exceptions are returned when the condition holds.

			returnsList.add(
					new BinaryExpression(
							LogicConnector.IMPLIES,
							r.when().getExpression(),
							WhyLocal.CAUGHT_EXCEPTION.isNullExpression()
					).toWhy(true).statement("ensures { ", " }")
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
						WhyLocal.CAUGHT_EXCEPTION.expression(),
						new WhyReference(r.getException()),
						true
				),
				r.getWhen().getExpression()
		);

		raisesList.add(expr.toWhy(true).statement("ensures { ", " }"));
	}

	public void visit() {
		final WhyFunctionSignature sig = function.contract().signature();

		if (sig.declaration().isProgram()) {
			// if this is a program function, require that the caught exception variable is null before calling the method
			visit(new WhyCondition.Requires(new WhyFunctionBody.SpecBody(WhyLocal.CAUGHT_EXCEPTION.isNullExpression())));
		}

		for (final WhyLocal p : sig.params()) {
			p.condition().ifPresent(c -> visit(new WhyCondition.Requires(new WhyFunctionBody.SpecBody(c))));
		}

		sig.resultParam().condition()
				.ifPresent(c -> visit(new WhyCondition.Ensures(new WhyFunctionBody.SpecBody(c), false)));

		for (final WhyCondition c : function.contract().conditions()) {
			visit(c);
		}

		function.body().ifPresent(b -> {
			if (decreasesList.isEmpty() && isRecursive) {
				decreasesList.add(line("variant { 0 } (* no variant on method *)"));
			}

			final Deque<WhySideEffects> effectsQueue = new ArrayDeque<>(List.of(b.sideEffects()));
			for (final WhyCondition c : function.contract().conditions()) {
				effectsQueue.add(c.sideEffects());
			}

			final Set<WhySideEffects> effectsSet = new HashSet<>();

			while (!effectsQueue.isEmpty()) {
				final WhySideEffects s = effectsQueue.removeLast();
				if (!effectsSet.add(s)) continue;

				for (final WhyFunctionSignature sign : s.calls()) {
					resolver.getBodySideEffects(sign).ifPresent(effectsQueue::add);
				}
			}

			final WhySideEffects sideEffects = WhySideEffects.combine(
					Stream.concat(
							function.contract().conditions()
							.stream()
							.map(WhyCondition::sideEffects),
							effectsSet.stream()
					).toList()
			);

			for (final String w : sideEffects.reads()) {
				readsList.add(line("reads { %s }".formatted(w)));
			}

			for (final String w : sideEffects.writes()) {
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
