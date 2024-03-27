package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.terminal;
import byteback.mlcfg.syntax.expr.transformer.ExpressionTransformer;
import byteback.mlcfg.syntax.expr.transformer.ExpressionVisitor;
import byteback.mlcfg.syntax.types.WhyJVMType;

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
