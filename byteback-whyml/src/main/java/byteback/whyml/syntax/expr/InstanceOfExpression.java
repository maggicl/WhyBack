package byteback.whyml.syntax.expr;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;

public class InstanceOfExpression implements Expression {
	private final Expression reference;
	private final WhyType type;

	public InstanceOfExpression(Expression reference, WhyType type) {
		if (reference.type() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("instanceof expression must have type PTR, given " + reference.type());
		}

		if (type.jvm() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("instanceof type to check must have JVM type PTR, given " + type.jvm());
		}

		this.reference = reference;
		this.type = type;
	}

	public Expression getReference() {
		return reference;
	}

	public WhyType getType() {
		return type;
	}

	@Override
	public SExpr toWhy() {
		return prefix(
				"instanceof",
				terminal("heap"),
				reference.toWhy(),
				terminal(type.getPreludeType())
		);
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.PTR;
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
