package byteback.mlcfg.syntax.expr.transformer;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.WhyFunctionSignature;
import byteback.mlcfg.syntax.expr.Expression;
import byteback.mlcfg.syntax.expr.FunctionCall;
import byteback.mlcfg.vimp.WhyResolver;
import java.util.Map;
import java.util.Set;

public class InlineCallTransformer extends ExpressionTransformer {
	private final WhyResolver resolver;
	private final Set<WhyFunctionSignature> toInline;

	public InlineCallTransformer(WhyResolver resolver, Set<WhyFunctionSignature> toInline) {
		this.resolver = resolver;
		this.toInline = toInline;
	}

	@Override
	public Expression transformFunctionCall(FunctionCall source) {
		if (!toInline.contains(source.function())) {
			return super.transformFunctionCall(source);
		}

		final Expression calleeBody = resolver.getFunction(source.function()).getBody();

		// replace in callee body all the formal parameters with the actual parameters
		return ParamActualizationTransformer.transform(source.argumentMap(), calleeBody);
	}

	public static Expression transform(Map<Identifier.L, Expression> argumentMap, Expression e) {
		return e.accept(new ParamActualizationTransformer(argumentMap));
	}
}
