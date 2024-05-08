package byteback.whyml.syntax.expr;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;

public record InstanceOfExpression(Expression reference, WhyType checkType) implements Expression {
	public InstanceOfExpression {
		if (reference.type() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("instanceof expression must have type PTR, given " + reference.type());
		}

		if (checkType.jvm() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("instanceof type to check must have JVM type PTR, given " + checkType.jvm());
		}
	}

	@Override
	public SExpr toWhy() {
		return prefix(
				"instanceof",
				terminal(Identifier.Special.HEAP),
				reference.toWhy(),
				checkType.getPreludeType()
		);
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.BOOL;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformInstanceOfExpression(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitInstanceOfExpression(this);
	}
}
