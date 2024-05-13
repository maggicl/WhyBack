package byteback.whyml.printer;

import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.Code.indent;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
import byteback.whyml.syntax.function.WhyFunctionContract;
import byteback.whyml.syntax.function.WhyFunctionDeclaration;
import byteback.whyml.vimp.VimpMethodNameParser;
import java.util.stream.Collectors;

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

		final WhyContractPrinter p = new WhyContractPrinter(isRecursive, m);
		p.visit();

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
				: ": %s".formatted(m.signature().returnType().getWhyType());

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
