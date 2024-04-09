package byteback.whyml.syntax.expr.transformer;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.LocalVariableExpression;
import java.util.Map;

public class ParamActualizationTransformer extends ExpressionTransformer {
	private final Map<Identifier.L, Expression> argumentMap;

	public ParamActualizationTransformer(Map<Identifier.L, Expression> argumentMap) {
		this.argumentMap = argumentMap;
	}

	@Override
	public Expression transformLocalVariableExpression(LocalVariableExpression source) {
		final Identifier.L name = source.getName();

		return argumentMap.containsKey(name)
				? argumentMap.get(name)
				: super.transformLocalVariableExpression(source);
	}

	public static Expression transform(Map<Identifier.L, Expression> argumentMap, Expression e) {
		return e.accept(new ParamActualizationTransformer(argumentMap));
	}
}
