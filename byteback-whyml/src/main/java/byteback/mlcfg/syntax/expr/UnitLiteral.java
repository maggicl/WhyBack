package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyJVMType;

public final class UnitLiteral implements Expression {
	public static final UnitLiteral INSTANCE = new UnitLiteral();

	private UnitLiteral() {
	}

	@Override
	public String toWhy() {
		return "()";
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.UNIT;
	}
}
