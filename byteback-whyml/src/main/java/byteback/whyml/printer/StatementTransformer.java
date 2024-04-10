package byteback.whyml.printer;

import byteback.whyml.identifiers.FQDNEscaper;
import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.expr.BooleanLiteral;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.LocalVariableExpression;
import byteback.whyml.syntax.expr.UnaryExpression;
import byteback.whyml.syntax.expr.transformer.ParamActualizationTransformer;
import byteback.whyml.syntax.function.VimpCondition;
import byteback.whyml.syntax.function.WhyFunctionParam;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.vimp.WhyResolver;
import java.util.Map;
import java.util.stream.Collectors;

class StatementTransformer implements VimpCondition.Transformer<Statement> {
	private final FQDNEscaper fqdnEscaper;
	private final WhyResolver resolver;
	private final WhyFunctionSignature m;

	public StatementTransformer(FQDNEscaper fqdnEscaper, WhyResolver resolver, WhyFunctionSignature m) {
		this.fqdnEscaper = fqdnEscaper;
		this.resolver = resolver;
		this.m = m;
	}

	private Expression resolveCondition(String name, boolean hasResult) {
		final Expression expression = resolver.resolveCondition(m, name, hasResult);

		if (!hasResult) {
			return expression;
		}

		// replace the last parameter in the predicate expression with the special `result` local variable
		// representing the return value in a WhyML condition
		final Identifier.L resultParam = Identifier.Special.methodParam(m.paramCount());
		final Map<Identifier.L, Expression> params = m.paramsWithResult(resultParam)
				.collect(Collectors.toMap(
						WhyFunctionParam::name,
						e -> new LocalVariableExpression(e.name() == resultParam
								? Identifier.Special.RESULT
								: e.name(),
								e.type().jvm())));

		return ParamActualizationTransformer.transform(params, expression);
	}

	@Override
	public Statement transformRequires(VimpCondition.Requires r) {
		return resolveCondition(r.getValue(), false).toWhy()
				.statement("requires { ", " }");
	}

	@Override
	public Statement transformEnsures(VimpCondition.Ensures r) {
		return resolveCondition(r.getValue(), true).toWhy()
				.statement("ensures { ", " }");
	}

	@Override
	public Statement transformReturns(VimpCondition.Returns r) {
		return r.getWhen().map(when -> (Expression) new UnaryExpression(
						UnaryExpression.Operator.NOT,
						resolveCondition(when, false)))
				.orElseGet(() -> new BooleanLiteral(true))
				.toWhy()
				.statement("raises { JException _ -> ", " }");
	}

	@Override
	public Statement transformRaises(VimpCondition.Raises r) {
		return r.getWhen().map(when -> resolveCondition(when, false))
				.orElseGet(() -> new BooleanLiteral(true))
				.toWhy()
				.statement(
						"raises { JException e -> e = %s.class && ".formatted(
								fqdnEscaper.escape(r.getException(), !r.getException().contains("."))),
						" }");
	}
}
