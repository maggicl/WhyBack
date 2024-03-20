package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;

public final class DoubleLiteral implements Expression {
	private final double value;

	public DoubleLiteral(double value) {
		this.value = value;
	}

	@Override
	public String toWhy() {
		if (Double.isNaN(value)) return "jdouble_nan";
		else if (Double.isInfinite(value)) return value > 0 ? "jdouble_inf" : "jdouble_minf";
		else {
			final long bits = Double.doubleToLongBits(value);

			boolean positive = (bits & 0x8000000000000000L) == 0;
			long exponent = ((bits & 0x7ff0000000000000L) >> 52) - 1023;
			boolean normalized = exponent > -1023L;
			long mantissa = bits & 0x000fffffffffffffL;

			// 13 * 4 = 52 => 13 hex digits needed to print the mantissa
			return "(0x%s%d.%013X%s:jdouble) (* %s *)".formatted(
					positive ? "" : "-",
					normalized ? 1 : 0,
					mantissa,
					normalized ? ("p" + exponent) : "",
					Double.toString(value)
			);
		}
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.DOUBLE;
	}
}