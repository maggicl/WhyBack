package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;

public final class FloatLiteral implements Expression {
	private final float value;

	public FloatLiteral(float value) {
		this.value = value;
	}

	@Override
	public String toWhy() {
		if (Float.isNaN(value)) return "jfloat_nan";
		else if (Float.isInfinite(value)) return value > 0 ? "jfloat_inf" : "jfloat_minf";
		else {
			final int bits = Float.floatToIntBits(value);

			boolean positive = (bits & 0x80000000) == 0;
			int exponent = ((bits & 0x7f800000) >> 23) - 127;
			boolean normalized = exponent > -127;
			int mantissa = bits & 0x007fffff;

			// ceil(23 / 4) = 6 => 6 hex digits needed to print the mantissa
			return "(0x%s%d.%06X%s:jfloat) (* %s *)".formatted(
					positive ? "" : "-",
					normalized ? 1 : 0,
					mantissa,
					normalized ? ("p" + exponent) : "",
					Float.toString(value)
			);
		}
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.FLOAT;
	}
}
