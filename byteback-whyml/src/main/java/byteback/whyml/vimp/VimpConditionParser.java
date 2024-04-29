package byteback.whyml.vimp;

import byteback.analysis.VimpCondition;
import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.expr.BooleanLiteral;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.LocalVariableExpression;
import byteback.whyml.syntax.expr.UnaryExpression;
import byteback.whyml.syntax.expr.transformer.ParamActualizationTransformer;
import byteback.whyml.syntax.function.WhyCondition;
import byteback.whyml.syntax.function.WhyFunctionParam;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import soot.SootMethod;

public class VimpConditionParser implements VimpCondition.Transformer<WhyCondition> {
	private final VimpClassNameParser vimpClassNameParser;
	private final VimpMethodParamParser paramParser;
	private final WhyResolver resolver;

	private final List<WhyFunctionParam> scopeParams;
	private final WhyType returnType;

	public VimpConditionParser(VimpClassNameParser vimpClassNameParser,
							   VimpMethodParamParser paramParser,
							   WhyResolver resolver,
							   List<WhyFunctionParam> scopeParams,
							   WhyType returnType) {
		this.vimpClassNameParser = vimpClassNameParser;
		this.paramParser = paramParser;
		this.resolver = resolver;
		this.scopeParams = scopeParams;
		this.returnType = returnType;
	}

	private Expression resolveCondition(SootMethod method, boolean hasResult) {
		final Expression expression = resolver.getSpecBody(method);

		final List<Identifier.L> condIdentifiers = paramParser.paramNames(method).toList();

		List<WhyFunctionParam> methodParams = hasResult && returnType != WhyJVMType.UNIT
				? Stream.concat(
				scopeParams.stream(),
				Stream.of(new WhyFunctionParam(Identifier.Special.RESULT_VAR, returnType, false))
		).toList() : scopeParams;

		if (condIdentifiers.size() != methodParams.size()) {
			throw new IllegalStateException("condIdentifiers and methodParams should have same length: condIdentifiers=" +
					condIdentifiers + " methodParams=" + methodParams);
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
	public WhyCondition.Requires transformRequires(VimpCondition.Requires r) {
		return new WhyCondition.Requires(resolveCondition(r.getValue(), false));
	}

	@Override
	public WhyCondition.Ensures transformEnsures(VimpCondition.Ensures r) {
		return new WhyCondition.Ensures(resolveCondition(r.getValue(), true));
	}

	@Override
	public WhyCondition.Decreases transformDecreases(VimpCondition.Decreases r) {
		return new WhyCondition.Decreases(resolveCondition(r.getValue(), false));
	}

	@Override
	public WhyCondition.Returns transformReturns(VimpCondition.Returns r) {
		return new WhyCondition.Returns(r.getWhen()
				.map(when -> (Expression) new UnaryExpression(
						UnaryExpression.Operator.NOT,
						resolveCondition(when, false)))
				.orElseGet(() -> new BooleanLiteral(true)));
	}

	@Override
	public WhyCondition.Raises transformRaises(VimpCondition.Raises r) {
		return new WhyCondition.Raises(r.getWhen().map(when -> resolveCondition(when, false))
				.orElseGet(() -> new BooleanLiteral(true)), vimpClassNameParser.parse(r.getException()));
	}
}
