package byteback.whyml.printer;

import byteback.whyml.identifiers.FQDNEscaper;
import byteback.whyml.identifiers.Identifier;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.expr.BooleanLiteral;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.LocalVariableExpression;
import byteback.whyml.syntax.expr.UnaryExpression;
import byteback.whyml.syntax.expr.transformer.ParamActualizationTransformer;
import byteback.whyml.syntax.function.VimpCondition;
import byteback.whyml.syntax.function.VimpMethod;
import byteback.whyml.syntax.function.VimpMethodParamNames;
import byteback.whyml.syntax.function.WhyFunctionParam;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.vimp.WhyResolver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class StatementTransformer implements VimpCondition.Transformer<Statement> {
	private final IdentifierEscaper identifierEscaper;
	private final FQDNEscaper fqdnEscaper;
	private final WhyResolver resolver;
	private final WhyFunctionSignature m;

	public StatementTransformer(IdentifierEscaper identifierEscaper, FQDNEscaper fqdnEscaper, WhyResolver resolver, WhyFunctionSignature m) {
		this.identifierEscaper = identifierEscaper;
		this.fqdnEscaper = fqdnEscaper;
		this.resolver = resolver;
		this.m = m;
	}

	private List<Identifier.L> nameIdentifiers(VimpMethodParamNames names) {
		return Stream.concat(
						names.thisName().stream(),
						names.parameterNames().stream()
				)
				.map(identifierEscaper::escapeL)
				.toList();
	}

	private Expression resolveCondition(String name, boolean hasResult) {
		final Map.Entry<VimpMethod, Expression> kv = resolver.resolveCondition(m, name, hasResult);
		final VimpMethod conditionRef = kv.getKey();
		final Expression expression = kv.getValue();

		if (conditionRef.names().isEmpty()) {
			throw new IllegalStateException("method parameter name information needed to actualize parameters in " +
					"condition");
		}

		final VimpMethodParamNames condNames = conditionRef.names().get();

		final List<Identifier.L> condIdentifiers = nameIdentifiers(condNames);
		final List<WhyFunctionParam> methodParams =
				(hasResult ? m.paramsWithResult(Identifier.Special.RESULT) : m.params()).toList();

		if (condIdentifiers.size() != methodParams.size()) {
			throw new IllegalStateException("condIdentifiers and methodParams should have same length");
		}

		// replace the last parameter in the predicate expression with the special `result` local variable
		// representing the return value in a WhyML condition. Also replace other param names in the condition method
		// with param names of the method itself for inlining
		final HashMap<Identifier.L, Expression> replacementMap = new HashMap<>();
		for (int i = 0; i < methodParams.size(); i++) {
			final WhyFunctionParam param = methodParams.get(i);
			replacementMap.put(condIdentifiers.get(i), new LocalVariableExpression(param.name(), param.type().jvm()));
		}

		return ParamActualizationTransformer.transform(replacementMap, expression);
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
		// TODO: check if multiple raises clauses with exception type conflict with each other
		// TODO: check if exact exception type or "instanceof" check (subclasses too) should be used here

		return r.getWhen().map(when -> resolveCondition(when, false))
				.orElseGet(() -> new BooleanLiteral(true))
				.toWhy()
				.statement(
						"raises { JException %s -> %s = %s.class && ".formatted(
								Identifier.Special.EXCEPTION_PARAM,
								Identifier.Special.EXCEPTION_PARAM,
								fqdnEscaper.escape(r.getException(), !r.getException().contains("."))),
						" }");
	}
}
