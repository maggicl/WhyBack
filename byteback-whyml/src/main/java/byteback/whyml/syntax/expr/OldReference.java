package byteback.whyml.syntax.expr;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

public record OldReference(Expression inner) implements Expression {
	@Override
	public SExpr toWhy(boolean useLogicOps) {
		return prefix("old", inner.toWhy(useLogicOps));
	}

	@Override
	public WhyJVMType type() {
		return inner.type();
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformOldReference(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitOldReference(this);
	}
}
