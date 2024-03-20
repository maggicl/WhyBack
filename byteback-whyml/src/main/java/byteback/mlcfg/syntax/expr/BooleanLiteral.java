package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;

public final class BooleanLiteral implements Expression {
	private final boolean value;

	public BooleanLiteral(boolean value) {
		this.value = value;
	}

	@Override
	public String toWhy() {
		return value ? "true" : "false";
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.BOOL;
	}
}
