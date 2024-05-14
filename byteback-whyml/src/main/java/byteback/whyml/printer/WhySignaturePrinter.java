package byteback.whyml.printer;

import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.Code.indent;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
import byteback.whyml.syntax.function.WhyFunction;
import byteback.whyml.syntax.function.WhyFunctionDeclaration;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.vimp.VimpMethodNameParser;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WhySignaturePrinter {
	private final VimpMethodNameParser vimpMethodNameParser;

	public WhySignaturePrinter(VimpMethodNameParser vimpMethodNameParser) {
		this.vimpMethodNameParser = vimpMethodNameParser;
	}

	public Code toWhy(WhyFunction function, boolean withWith, boolean isRecursive) {
		final WhyFunctionSignature sig = function.contract().signature();

		final String params = Stream.concat(
				Stream.of("(ghost %s: Heap.t)".formatted(Identifier.Special.HEAP)),
				sig.params().stream()
						.map(e -> "(%s: %s)".formatted(e.name(), e.type().getWhyType()))
		).collect(Collectors.joining(" "));

		final WhyContractPrinter p = new WhyContractPrinter(isRecursive, function);
		p.visit();

		final String declaration = function.body().isPresent()
				? sig.declaration().toWhy(withWith, isRecursive)
				: sig.declaration().toWhyDeclaration();

		return many(
				line("%s %s %s : %s".formatted(
						declaration,
						vimpMethodNameParser.methodName(sig),
						params,
						sig.returnType().getWhyType()
				).trim()),
				indent(p.conditionStatements())
		);
	}
}
