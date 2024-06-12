package byteback.whyml.syntax.expr;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

public record DoubleLiteral(double value) implements Expression {
	@Override
	public SExpr toWhy(boolean useLogicOps) {
		if (Double.isNaN(value)) return terminal("jdouble_nan");
		else if (Double.isInfinite(value)) return terminal(value > 0 ? "jdouble_inf" : "jdouble_minf");
		else {
			final long bits = Double.doubleToLongBits(value);

			boolean positive = (bits & 0x8000000000000000L) == 0;
			long exponent = ((bits & 0x7ff0000000000000L) >> 52) - 1023;
			boolean normalized = exponent > -1023L;
			long mantissa = bits & 0x000fffffffffffffL;

			// 13 * 4 = 52 => 13 hex digits needed to print the mantissa
			return terminal("(0x%s%d.%013X%s:jdouble) (* %s *)".formatted(
					positive ? "" : "-",
					normalized ? 1 : 0,
					mantissa,
					normalized ? ("p" + exponent) : "",
					Double.toString(value)
			));
		}
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.DOUBLE;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformDoubleLiteral(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitDoubleLiteral(this);
	}
}
