package byteback.whyml.printer;

import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.Code.block;
import static byteback.whyml.printer.Code.indent;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
import byteback.whyml.syntax.function.WhyCondition;
import byteback.whyml.syntax.function.WhyFunctionContract;
import byteback.whyml.syntax.function.WhyFunctionDeclaration;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.vimp.VimpMethodNameParser;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WhySignaturePrinter {
	private final VimpMethodNameParser vimpMethodNameParser;

	public WhySignaturePrinter(VimpMethodNameParser vimpMethodNameParser) {
		this.vimpMethodNameParser = vimpMethodNameParser;
	}

	public Code toWhy(WhyFunctionContract m, boolean noPredicates, boolean withWith, boolean isRecursive, boolean hasBody) {
		final boolean isPredicate = !noPredicates && m.signature().declaration() == WhyFunctionDeclaration.PREDICATE;

		final String params = m.signature().params().stream()
				.map(e -> "(%s: %s)".formatted(e.name(), e.type().getWhyType()))
				.collect(Collectors.joining(" "));

		final Stream<WhyCondition> paramPreconditions = m.signature().params().stream()
				.map(WhyLocal::condition)
				.flatMap(Optional::stream)
				.map(WhyCondition.Requires::new);

		final Stream<WhyCondition> resultPostcondition = m.signature().resultParam().condition()
						.stream()
						.map(WhyCondition.Ensures::new);


		final WhyConditionsPrinter p = new WhyConditionsPrinter(isRecursive, m.signature().declaration().isSpec());
		p.print(Stream.concat(
					Stream.concat(m.conditions().stream(), paramPreconditions),
				resultPostcondition));

		final String declaration;
		if (withWith) {
			declaration = m.signature().declaration().isSpec() ? "with ghost" : "with";
		} else {
			final WhyFunctionDeclaration declType = m.signature().declaration();
			declaration = declType.isSpec() || hasBody
					? declType.toWhy(isRecursive)
					: declType.toWhyDeclaration();
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
				indent(p.conditionStatements())
		);
	}
}
