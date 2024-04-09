package byteback.whyml.syntax.expr;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

public final class UnitLiteral implements Expression {
	public static final UnitLiteral INSTANCE = new UnitLiteral();

	private UnitLiteral() {
	}

	@Override
	public SExpr toWhy() {
		return terminal("()");
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.UNIT;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformUnitLiteral(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitUnitLiteral(this);
	}
}
