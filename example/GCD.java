/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Special.*;

public class GCD {

	@Pure
	public static short gcd_recursive(short a, short b) {
		return conditional(eq(a, b), a, conditional(gt(a, b), gcd_recursive2((short) (a - b), b), gcd_recursive2(a, (short) (b - a))));
	}

	@Pure
	public static short test(short a) {
		return conditional(lte(a, (short) 0), (short) 0, test((short)(a - 1)));
	}

	@Pure
	public static short gcd_recursive2(short a, short b) {
		return conditional(eq(a, b), a, conditional(gt(a, b), gcd_recursive((short) (a - b), b), gcd_recursive(a, (short) (b - a))));
	}
	@Predicate
	public static boolean result_is_gcd(short a, short b, short r) {
		return implies(not(arguments_are_negative(a, b)), eq(r, gcd_recursive(a, b)));
	}

	@Pure
	@Predicate
	public static boolean arguments_are_negative(short a, short b) {
		return lte(a, 0) | lte(b, 0);
	}

	@Pure
	@Predicate
	public static boolean arguments_are_positive(short a, short b) {
		return lte(a, 0) & lte(b, 0);
	}

	@Raise(exception = IllegalArgumentException.class, when = "arguments_are_negative")
	@Ensure("result_is_gcd")
	public static short gcd(final short a, final short b) {

		if (a <= 0 || b <= 0) {
			throw new IllegalArgumentException("Both arguments must be positive");
		}

		if ((long) a > (long) "ciao".length()) {
			System.out.println("aaaa");
		}

		if (0.1 % Double.NaN == 0.0) {
			System.out.println("bbbb");
		}

		short r = a;
		short x = b;

		while (r != x) {
			invariant(gt(r, 0) & gt(x, 0));
			invariant(eq(gcd_recursive(r, x), gcd_recursive(a, b)));

			if (r > x) {
				r = (short) (r - x);
			} else {
				x = (short) (x - r);
			}
		}

		return r;
	}

}
/**
 * RUN: %{verify} %t.bpl | filecheck %s
 * CHECK: Boogie program verifier finished with 2 verified, 0 errors
 */
