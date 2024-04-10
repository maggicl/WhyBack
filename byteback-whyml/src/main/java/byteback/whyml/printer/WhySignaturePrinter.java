package byteback.whyml.printer;

import byteback.whyml.identifiers.FQDNEscaper;
import byteback.whyml.identifiers.Identifier;
import static byteback.whyml.printer.Statement.block;
import static byteback.whyml.printer.Statement.indent;
import static byteback.whyml.printer.Statement.line;
import static byteback.whyml.printer.Statement.many;
import byteback.whyml.syntax.expr.BooleanLiteral;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.UnaryExpression;
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
		final WhyFunctionKind kind = noPredicates && m.kind() == WhyFunctionKind.PURE_PREDICATE
				? WhyFunctionKind.PURE
				: m.kind();

		final List<WhyFunctionParam> paramsList = m.params().toList();

		final String params = paramsList.stream()
				.map(e -> "(%s: %s)".formatted(e.name(), e.type().getWhyType()))
				.collect(Collectors.joining(" "));

		final Statement paramPreconditions = many(paramsList.stream()
				.map(WhyFunctionParam::condition)
				.flatMap(Optional::stream)
				.map(e -> line("requires { %s }".formatted(e))));

		final VimpCondition.Transformer<Statement> toStatement = new VimpCondition.Transformer<>() {
			@Override
			public Statement transformRequires(VimpCondition.Requires r) {
				return resolver.resolveCondition(m.vimp(), r.getValue(), false).toWhy()
						.statement("requires {", "}");
			}

			@Override
			public Statement transformEnsures(VimpCondition.Ensures r) {
				return resolver.resolveCondition(m.vimp(), r.getValue(), true).toWhy()
						.statement("ensures {", "}");
			}

			@Override
			public Statement transformReturns(VimpCondition.Returns r) {
				return r.getWhen().map(when -> (Expression) new UnaryExpression(
								UnaryExpression.Operator.NOT,
								resolver.resolveCondition(m.vimp(), when, false)))
						.orElseGet(() -> new BooleanLiteral(true))
						.toWhy()
						.statement("raises { JException _ -> ", " }");
			}

			@Override
			public Statement transformRaises(VimpCondition.Raises r) {
				return r.getWhen().map(when -> resolver.resolveCondition(m.vimp(), when, false))
						.orElseGet(() -> new BooleanLiteral(true))
						.toWhy()
						.statement(
								"raises { JException e -> e = %s.class && ".formatted(
										fqdnEscaper.escape(r.getException(), !r.getException().contains("."))),
								" }");
			}
		};

		final Statement conditions = many(m.conditions().stream().map(toStatement::transform));

		final Statement resultPostcondition = many(
				new WhyFunctionParam(Identifier.Special.RESULT, m.returnType(), false)
						.condition()
						.map("ensures { %s }"::formatted)
						.map(Statement::line)
						.stream());

		// TODO: capture variants
		final Statement variant = recursive ? line("variant { 0 }") : many();

		final String declaration;

		if (withWith) {
			declaration = "with";
		} else {
			declaration = recursive ? kind.getWhyRecDeclaration() : kind.getWhyDeclaration();
		}

		final String returnType = kind != WhyFunctionKind.PURE_PREDICATE
				? ": %s".formatted(m.returnType().getWhyType())
				: "";

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
