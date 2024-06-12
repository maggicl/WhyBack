package byteback.whyml.syntax.expr;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

public record FloatLiteral(float value) implements Expression {
	@Override
	public SExpr toWhy(boolean useLogicOps) {
		if (Float.isNaN(value)) return terminal("jfloat_nan");
		else if (Float.isInfinite(value)) return terminal(value > 0 ? "jfloat_inf" : "jfloat_minf");
		else {
			final int bits = Float.floatToIntBits(value);

			boolean positive = (bits & 0x80000000) == 0;
			int exponent = ((bits & 0x7f800000) >> 23) - 127;
			boolean normalized = exponent > -127;
			int mantissa = (bits & 0x007fffff) << 1;

			// ceil(23 / 4) = 6 => 6 hex digits needed to print the mantissa
			return terminal("(0x%s%d.%06X%s:jfloat) (* %s *)".formatted(
					positive ? "" : "-",
					normalized ? 1 : 0,
					mantissa,
					normalized ? ("p" + exponent) : "",
					Float.toString(value)
			));
		}
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.FLOAT;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformFloatLiteral(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitFloatLiteral(this);
	}
}
