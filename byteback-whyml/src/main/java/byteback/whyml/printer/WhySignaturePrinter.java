package byteback.whyml.printer;

import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.Statement.block;
import static byteback.whyml.printer.Statement.indent;
import static byteback.whyml.printer.Statement.line;
import static byteback.whyml.printer.Statement.many;
import byteback.whyml.syntax.function.WhyFunctionContract;
import byteback.whyml.syntax.function.WhyFunctionDeclaration;
import byteback.whyml.syntax.function.WhyFunctionParam;
import byteback.whyml.vimp.VimpMethodNameParser;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WhySignaturePrinter {
	private final VimpMethodNameParser vimpMethodNameParser;

	public WhySignaturePrinter(VimpMethodNameParser vimpMethodNameParser) {
		this.vimpMethodNameParser = vimpMethodNameParser;
	}

	public Statement toWhy(WhyFunctionContract m, boolean noPredicates, boolean withWith, boolean isRecursive) {
		final boolean isPredicate = !noPredicates && m.signature().declaration() == WhyFunctionDeclaration.PREDICATE;

		final String params = m.signature().params().stream()
				.map(e -> "(%s: %s)".formatted(e.name(), e.type().getWhyType()))
				.collect(Collectors.joining(" "));

		final Statement paramPreconditions = many(m.signature().params().stream()
				.map(WhyFunctionParam::condition)
				.flatMap(Optional::stream)
				.map(e -> line("requires { %s }".formatted(e))));

		final Statement resultPostcondition = many(
				new WhyFunctionParam(Identifier.Special.RESULT, m.signature().returnType(), false)
						.condition()
						.map("ensures { %s }"::formatted)
						.map(Statement::line)
						.stream());

		final WhyConditionsPrinter p = new WhyConditionsPrinter(isRecursive);
		p.print(m.conditions());

		final String declaration;
		if (withWith) {
			declaration = m.signature().declaration().isSpec() ? "with ghost" : "with";
		} else {
			final WhyFunctionDeclaration declType = m.signature().declaration();
			declaration = declType.isSpec() ? declType.toWhy(isRecursive) : declType.toWhyDeclaration();
		}

		final String returnType = isPredicate
				? ""
				: (m.signature().declaration().isSpec() ? ": %s" : ": Result.t %s")
					.formatted(m.signature().returnType().getWhyType());

		return many(
				line("%s %s (ghost %s: Heap.t)%s %s".formatted(
						declaration,
						vimpMethodNameParser.methodName(m.signature()),
						Identifier.Special.HEAP,
						params.isEmpty() ? "" : (" " + params),
						returnType).trim()),
				indent(paramPreconditions, resultPostcondition, p.conditionStatements())
		);
	}

	public Statement toWhy(WhyFunctionContract m) {
		return toWhy(m, false, false, false);
	}

	public Statement toWhy(Identifier.FQDN declaringClass, List<WhyFunctionContract> methods) {
		final WhyClassScope scope = new WhyClassScope(declaringClass);
		return scope.with(block(methods.stream().map(this::toWhy).map(Statement::block)));
	}
}
