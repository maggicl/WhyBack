package byteback.whyml.vimp;

import byteback.analysis.VimpCondition;
import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.expr.BooleanLiteral;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.LocalExpression;
import byteback.whyml.syntax.expr.UnaryExpression;
import byteback.whyml.syntax.expr.transformer.ParamActualizationTransformer;
import byteback.whyml.syntax.function.WhyCondition;
import byteback.whyml.syntax.function.WhyFunction;
import byteback.whyml.syntax.function.WhyFunctionBody;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.syntax.type.WhyJVMType;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import soot.SootMethod;

public class VimpConditionParser implements VimpCondition.Transformer<WhyCondition> {
	private final VimpClassNameParser vimpClassNameParser;
	private final VimpMethodParamParser paramParser;
	private final WhyResolver resolver;
	private final WhyFunctionSignature signature;

	public VimpConditionParser(VimpClassNameParser vimpClassNameParser,
							   VimpMethodParamParser paramParser,
							   WhyResolver resolver,
							   WhyFunctionSignature signature) {
		this.vimpClassNameParser = vimpClassNameParser;
		this.paramParser = paramParser;
		this.resolver = resolver;
		this.signature = signature;
	}

	private Expression resolveCondition(SootMethod method, boolean hasResult) {
		final WhyFunctionBody.SpecBody expression = resolver.getSpecBody(method);

		final List<Identifier.L> condIdentifiers = paramParser.paramNames(method).toList();

		List<WhyLocal> methodParams = hasResult && signature.returnType() != WhyJVMType.UNIT
				? Stream.concat(
				signature.params().stream(),
				Stream.of(signature.resultParam())
		).toList()
				: signature.params();

		if (condIdentifiers.size() != methodParams.size()) {
			throw new IllegalStateException("condIdentifiers and methodParams should have same length: condIdentifiers=" +
					condIdentifiers + " methodParams=" + methodParams);
		}

		// replace the last parameter in the predicate expression with the special `result` local variable
		// representing the return value in a WhyML condition. Also replace other param names in the condition method
		// with param names of the method itself for inlining
		final HashMap<Identifier.L, Expression> replacementMap = new HashMap<>();
		for (int i = 0; i < methodParams.size(); i++) {
			final WhyLocal param = methodParams.get(i);
			replacementMap.put(condIdentifiers.get(i), new LocalExpression(param.name(), param.type().jvm()));
		}

		return ParamActualizationTransformer.transform(
				replacementMap,
				expression.getExpression()
		);
	}

	@Override
	public WhyCondition.Requires transformRequires(VimpCondition.Requires r) {
		return new WhyCondition.Requires(new WhyFunctionBody.SpecBody(resolveCondition(r.getValue(), false)));
	}

	@Override
	public WhyCondition.Ensures transformEnsures(VimpCondition.Ensures r) {
		return new WhyCondition.Ensures(new WhyFunctionBody.SpecBody(resolveCondition(r.getValue(), true)));
	}

	@Override
	public WhyCondition.Decreases transformDecreases(VimpCondition.Decreases r) {
		return new WhyCondition.Decreases(new WhyFunctionBody.SpecBody(resolveCondition(r.getValue(), false)));
	}

	@Override
	public WhyCondition.Returns transformReturns(VimpCondition.Returns r) {
		return new WhyCondition.Returns(r.getWhen()
				.map(when -> new WhyFunctionBody.SpecBody(new UnaryExpression(
						UnaryExpression.Operator.NOT,
						resolveCondition(when, false))))
				.orElseGet(() -> new WhyFunctionBody.SpecBody(BooleanLiteral.of(true))));
	}

	@Override
	public WhyCondition.Raises transformRaises(VimpCondition.Raises r) {
		return new WhyCondition.Raises(
				new WhyFunctionBody.SpecBody(
						r.getWhen()
							.map(when -> resolveCondition(when, false))
							.orElseGet(() -> BooleanLiteral.of(true))
				), vimpClassNameParser.parse(r.getException())
		);
	}
}
