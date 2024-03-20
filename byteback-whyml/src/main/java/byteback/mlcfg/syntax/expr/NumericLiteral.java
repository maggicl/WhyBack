package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;
import java.util.EnumSet;

public final class NumericLiteral implements Expression {
	private static final EnumSet<WhyJVMType> NUMERICS =
			EnumSet.complementOf(EnumSet.of(WhyJVMType.INT, WhyJVMType.LONG));

	private final WhyJVMType type;
	private final long value;

	public NumericLiteral(WhyJVMType type, long value) {
		if (!NUMERICS.contains(type)) {
			throw new IllegalArgumentException("literal has not valid numeric type: " + type);
		}

		this.type = type;
		this.value = value;
	}

	@Override
	public String toWhy() {
		return "(%d:%s)".formatted(value, type.getWhyType());
	}

	@Override
	public WhyJVMType type() {
		return type;
	}
}
