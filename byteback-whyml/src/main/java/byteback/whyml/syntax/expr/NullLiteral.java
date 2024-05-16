package byteback.whyml.syntax.expr;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

public final class NullLiteral implements Expression {
	public static final NullLiteral INSTANCE = new NullLiteral();

	private NullLiteral() {
	}

	@Override
	public SExpr toWhy() {
		return terminal("Ptr.null");
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.PTR;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformNullLiteral(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitNullLiteral(this);
	}

	@Override
	public String toString() {
		return "NullLiteral{}";
	}
}
