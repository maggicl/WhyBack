package byteback.whyml.syntax.expr;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

public final class BooleanLiteral implements Expression {
	private final boolean value;

	public BooleanLiteral(boolean value) {
		this.value = value;
	}

	@Override
	public SExpr toWhy() {
		return terminal(value ? "true" : "false");
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.BOOL;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformBooleanLiteral(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitBooleanLiteral(this);
	}
}
