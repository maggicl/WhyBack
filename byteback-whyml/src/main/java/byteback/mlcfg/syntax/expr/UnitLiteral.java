package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.terminal;
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
}
