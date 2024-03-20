package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyPrimitive;
import byteback.mlcfg.syntax.types.WhyType;
import java.util.EnumSet;

public final class NumericLiteral implements Expression {
	private static final EnumSet<WhyPrimitive> NUMERICS =
			EnumSet.complementOf(EnumSet.of(WhyPrimitive.BOOL, WhyPrimitive.FLOAT, WhyPrimitive.DOUBLE));

	private final WhyPrimitive type;
	private final long value;

	public NumericLiteral(WhyPrimitive type, long value) {
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
	public WhyType type() {
		return type;
	}
}
