package byteback.whyml.syntax.expr;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyReference;

public record NewExpression(WhyReference objType) implements Expression {
	@Override
	public SExpr toWhy() {
		return SExpr.prefix("new", terminal(Identifier.Special.HEAP), objType.getPreludeClassType());
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.PTR;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformNewExpression(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitNewExpression(this);
	}
}
