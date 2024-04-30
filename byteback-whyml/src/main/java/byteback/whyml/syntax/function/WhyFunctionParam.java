package byteback.whyml.syntax.function;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.InstanceOfExpression;
import byteback.whyml.syntax.expr.LocalVariableExpression;
import byteback.whyml.syntax.expr.UnaryExpression;
import byteback.whyml.syntax.expr.binary.BinaryExpression;
import byteback.whyml.syntax.expr.binary.LogicConnector;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;
import java.util.Optional;

public record WhyFunctionParam(Identifier.L name, WhyType type, boolean isThis) {
	public Optional<Expression> condition() {
		if (type.jvm() != WhyJVMType.PTR) {
			return Optional.empty();
		}

		final Expression var = new LocalVariableExpression(name, type.jvm());
		final Expression isType = new InstanceOfExpression(var, type);

		return Optional.of(isThis
				? new BinaryExpression(LogicConnector.AND,
				new UnaryExpression(UnaryExpression.Operator.COND_IFNOTNULL, var),
				isType)
				: isType);
	}
}
