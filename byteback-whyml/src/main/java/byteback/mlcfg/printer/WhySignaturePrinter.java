package byteback.mlcfg.printer;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.indent;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.syntax.WhyCondition;
import byteback.mlcfg.syntax.WhyFunctionKind;
import byteback.mlcfg.syntax.WhyFunctionParam;
import byteback.mlcfg.syntax.WhyFunctionSignature;
import byteback.mlcfg.syntax.expr.BooleanLiteral;
import byteback.mlcfg.syntax.expr.Expression;
import byteback.mlcfg.syntax.expr.UnaryExpression;
import byteback.mlcfg.vimp.WhyResolver;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WhySignaturePrinter {

	private final IdentifierEscaper identifierEscaper;

	public WhySignaturePrinter(IdentifierEscaper identifierEscaper) {
		this.identifierEscaper = identifierEscaper;
	}

	public Statement toWhy(WhyFunctionSignature m, boolean forScc, boolean withWith, boolean recursive, WhyResolver resolver) {
		if (forScc && !m.kind().isSpec()) {
			throw new IllegalArgumentException("SCC signature mode supported only for spec functionList");
		}

		final List<WhyFunctionParam> paramsList = m.params().toList();

		final String params = paramsList.stream()
				.map(e -> "(%s: %s)".formatted(e.name(), e.type().getWhyType()))
				.collect(Collectors.joining(" "));

		final Statement paramPreconditions = many(paramsList.stream()
				.map(WhyFunctionParam::condition)
				.flatMap(Optional::stream)
				.map(e -> line("requires { %s }".formatted(e))));

		final WhyCondition.Transformer<Statement> toStatement = new WhyCondition.Transformer<>() {
			@Override
			public Statement transformRequires(WhyCondition.Requires r) {
				return resolver.resolveCondition(m.reference(), r.getValue()).toWhy()
						.statement("requires {", "}");
			}

			@Override
			public Statement transformEnsures(WhyCondition.Ensures r) {
				return resolver.resolveCondition(m.reference(), r.getValue()).toWhy()
						.statement("ensures {", "}");
			}

			@Override
			public Statement transformReturns(WhyCondition.Returns r) {
				return r.getWhen().map(when -> (Expression) new UnaryExpression(
								UnaryExpression.Operator.NOT,
								resolver.resolveCondition(m.reference(), when)))
						.orElseGet(() -> new BooleanLiteral(true))
						.toWhy()
						.statement("raises { ", " }");
			}

			@Override
			public Statement transformRaises(WhyCondition.Raises r) {
				// TODO: fix exception identifier
				return r.getWhen().map(when -> resolver.resolveCondition(m.reference(), when))
						.orElseGet(() -> new BooleanLiteral(true))
						.toWhy()
						.statement("raises { " + r.getException() + " -> ", " }");
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
		final Statement variant = forScc && recursive ? line("variant { 0 }") : many();

		final String declaration;

		if (withWith) {
			declaration = "with";
		} else {
			final WhyFunctionKind kindToUse = forScc ? WhyFunctionKind.PURE : m.kind();
			declaration = recursive
					? kindToUse.getWhyRecDeclaration()
					: kindToUse.getWhyDeclaration();
		}

		final String returnType = m.kind() != WhyFunctionKind.PURE_PREDICATE || forScc
				? " : %s".formatted(m.returnType().getWhyType())
				: "";

		final Identifier.L name = identifierEscaper.escapeL(m.reference().methodName() + m.reference().descriptor());
		final Identifier.L declName = forScc ? identifierEscaper.specFunction(m.reference().className(), name) : name;

		return many(
				line("%s %s (ghost heap: Heap.t) %s%s".formatted(declaration, declName, params, returnType)),
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
