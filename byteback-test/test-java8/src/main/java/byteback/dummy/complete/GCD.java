/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %s.actual.bpl 2>&1 | filecheck %s
 * RUN: diff %s.actual.bpl %s.expect.bpl
 */
package byteback.dummy.complete;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Special.*;

public class GCD {

	@Pure
	public static int gcd_recursive(int a, int b) {
		return conditional(eq(a, b), a, conditional(gt(a, b), gcd_recursive(a - b, b), gcd_recursive(a, b - a)));
	}

	@Predicate
	public static boolean result_is_gcd(int a, int b, int r) {
		return eq(r, gcd_recursive(a, b));
	}

	@Predicate
	public static boolean arguments_are_positive(int a, int b) {
		return gt(a, 0) & gt(b, 0);
	}

	@Require("arguments_are_positive")
	@Ensure("result_is_gcd")
	public static int gcd(final int a, final int b) {
		int r = a;
		int x = b;

		while (r != x) {
			invariant(gt(r, 0) & gt(x, 0));
			invariant(eq(gcd_recursive(r, x), gcd_recursive(a, b)));

			if (r > x) {
				r = r - x;
			} else {
				x = x - r;
			}
		}

		return r;
	}

}
/**
 * CHECK: Conversion completed
 * RUN: %{verify} %s.actual.bpl
 */
