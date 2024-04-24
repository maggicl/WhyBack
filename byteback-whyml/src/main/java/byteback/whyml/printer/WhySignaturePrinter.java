package byteback.whyml.printer;

import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.Statement.block;
import static byteback.whyml.printer.Statement.indent;
import static byteback.whyml.printer.Statement.line;
import static byteback.whyml.printer.Statement.many;
import byteback.whyml.syntax.function.WhyFunctionDeclaration;
import byteback.whyml.syntax.function.WhyFunctionParam;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.vimp.VimpMethodNameParser;
import byteback.whyml.vimp.WhyResolver;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WhySignaturePrinter {
	private final VimpMethodNameParser vimpMethodNameParser;

	public WhySignaturePrinter(VimpMethodNameParser vimpMethodNameParser) {
		this.vimpMethodNameParser = vimpMethodNameParser;
	}

	public Statement toWhy(WhyFunctionSignature m, boolean noPredicates, boolean withWith, boolean recursive, WhyResolver resolver) {
		final boolean isPredicate = !noPredicates && m.declaration() == WhyFunctionDeclaration.PREDICATE;

		final String params = m.params().stream()
				.map(e -> "(%s: %s)".formatted(e.name(), e.type().getWhyType()))
				.collect(Collectors.joining(" "));

		final Statement paramPreconditions = many(m.params().stream()
				.map(WhyFunctionParam::condition)
				.flatMap(Optional::stream)
				.map(e -> line("requires { %s }".formatted(e))));

		final Statement resultPostcondition = many(
				new WhyFunctionParam(Identifier.Special.RESULT, m.returnType(), false)
						.condition()
						.map("ensures { %s }"::formatted)
						.map(Statement::line)
						.stream());

		final WhyConditionsPrinter p = new WhyConditionsPrinter();
		p.print(m.conditions());

		// TODO: capture variants
		final Statement variant = recursive ? line("variant { 0 }") : many();

		final String declaration = withWith
				? "with"
				: m.declaration().isSpec()
				? m.declaration().toWhy(recursive)
				: m.declaration().toWhyDeclaration();

		final String returnType = isPredicate ? "" : ": %s".formatted(m.returnType().getWhyType());

		return many(
				line("%s %s (ghost %s: Heap.t) %s %s".formatted(
						declaration,
						vimpMethodNameParser.methodName(m),
						Identifier.Special.HEAP,
						params,
						returnType).trim()),
				indent(paramPreconditions, resultPostcondition, p.conditionStatements(), variant)
		);
	}

	public Statement toWhy(WhyFunctionSignature m, WhyResolver resolver) {
		return toWhy(m, false, false, false, resolver);
	}

	public Statement toWhy(Identifier.FQDN declaringClass, List<WhyFunctionSignature> methods, WhyResolver resolver) {
		final WhyClassScope scope = new WhyClassScope(declaringClass);
		return scope.with(block(methods.stream().map(e -> toWhy(e, resolver)).map(Statement::block)));
	}
}
