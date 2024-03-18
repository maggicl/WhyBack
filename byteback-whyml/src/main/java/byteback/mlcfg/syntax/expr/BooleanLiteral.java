package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyPrimitive;
import byteback.mlcfg.syntax.types.WhyType;

public final class BooleanLiteral extends Expression {
	private final boolean value;

	public BooleanLiteral(boolean value) {
		this.value = value;
	}

	@Override
	public String toWhy() {
		return value ? "true" : "false";
	}

	@Override
	public WhyType type() {
		return WhyPrimitive.BOOL;
	}
}
