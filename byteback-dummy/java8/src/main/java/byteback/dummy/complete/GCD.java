package byteback.dummy.complete;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Special.*;

public class GCD {

	@Pure
	public static int gcd_recursive(int a, int b) {
		return conditional(eq(a, b), a,
											 conditional(gt(a, b), gcd_recursive(a - b, b),
																	 gcd_recursive(a, b - a)));
	}

	public static int gcd(int a, int b) {
		int c;

		while (b != 0) {
			c = a % b;
			a = b;
			b = c;
		}

		return a;
	}

}
