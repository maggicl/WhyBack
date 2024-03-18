package byteback.mlcfg;

import java.util.Collections;
import java.util.stream.Stream;

public final class Utils {
	private Utils() {
	}

	public static Stream<String> repeat(int times, String s) {
		return Collections.nCopies(times, s).stream();
	}

	public static String trimToNull(String e) {
		return e.isEmpty() ? null : e;
	}

	public static String doubleToBinary(double d) {
		if (Double.isNaN(d)) return "jdouble_nan";
		else if (Double.isInfinite(d)) return d > 0 ? "jdouble_inf" : "jdouble_minf";
		else {
			final long bits = Double.doubleToLongBits(d);

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
					Double.toString(d)
			);
		}
	}

	public static void main(String[] args) {
		System.out.println(doubleToBinary(5.0));
		System.out.println(doubleToBinary(13.0));
		System.out.println(doubleToBinary(5.0 / 13.0));
		System.out.println(doubleToBinary(1.0 / 0.0));
		System.out.println(doubleToBinary(1.5));
		System.out.println(doubleToBinary(4.0));
		System.out.println(doubleToBinary(Double.MIN_VALUE));
	}
}
