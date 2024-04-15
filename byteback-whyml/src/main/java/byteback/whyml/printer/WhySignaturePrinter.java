package byteback.whyml.printer;

import byteback.whyml.identifiers.FQDNEscaper;
import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.Statement.block;
import static byteback.whyml.printer.Statement.indent;
import static byteback.whyml.printer.Statement.line;
import static byteback.whyml.printer.Statement.many;
import byteback.whyml.syntax.function.VimpCondition;
import byteback.whyml.syntax.function.WhyFunctionKind;
import byteback.whyml.syntax.function.WhyFunctionParam;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.vimp.VimpMethodNameParser;
import byteback.whyml.vimp.WhyResolver;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WhySignaturePrinter {
	private final FQDNEscaper fqdnEscaper;

	private final VimpMethodNameParser vimpMethodNameParser;

	public WhySignaturePrinter(FQDNEscaper fqdnEscaper, VimpMethodNameParser vimpMethodNameParser) {
		this.fqdnEscaper = fqdnEscaper;
		this.vimpMethodNameParser = vimpMethodNameParser;
	}

	public Statement toWhy(WhyFunctionSignature m, boolean noPredicates, boolean withWith, boolean recursive, WhyResolver resolver) {
		if (m.kind().inline().must()) {
			throw new IllegalArgumentException("function signature cannot be printed for an inline-required function");
		}

		final boolean isPredicate = !noPredicates && m.kind().decl() == WhyFunctionKind.Declaration.PREDICATE;

		final List<WhyFunctionParam> paramsList = m.params().toList();

		final String params = paramsList.stream()
				.map(e -> "(%s: %s)".formatted(e.name(), e.type().getWhyType()))
				.collect(Collectors.joining(" "));

		final Statement paramPreconditions = many(paramsList.stream()
				.map(WhyFunctionParam::condition)
				.flatMap(Optional::stream)
				.map(e -> line("requires { %s }".formatted(e))));

		final VimpCondition.Transformer<Statement> toStatement = new StatementTransformer(fqdnEscaper, resolver, m);

		final Statement conditions = many(m.conditions().stream().map(toStatement::transform));

		final Statement resultPostcondition = many(
				new WhyFunctionParam(Identifier.Special.RESULT, m.returnType(), false)
						.condition()
						.map("ensures { %s }"::formatted)
						.map(Statement::line)
						.stream());

		// TODO: capture variants
		final Statement variant = recursive ? line("variant { 0 }") : many();

		final String declaration = withWith
				? "with"
				: m.kind().decl().isSpec()
				? m.kind().decl().toWhy(recursive)
				: m.kind().decl().toWhyDeclaration();

		final String returnType = isPredicate ? "" : ": %s".formatted(m.returnType().getWhyType());

		return many(
				line("%s %s (ghost heap: Heap.t) %s %s".formatted(
						declaration,
						vimpMethodNameParser.methodName(m.vimp()),
						params,
						returnType).trim()),
				indent(paramPreconditions, resultPostcondition, conditions, variant)
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
