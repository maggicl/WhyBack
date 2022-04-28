package byteback.dummy.complete;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;

import byteback.annotations.Contract.Ensure;
import byteback.annotations.Contract.Require;

public class GCD {

	@Pure
	public static boolean divides(int a, int b) {
		return eq(a % b, 0);
	}

	@Condition
	public static boolean arguments_are_positive(int a, int b) {
		return gt(a, 0) & gt(b, 0);
	}

	@Condition
	public static boolean result_is_positive(int a, int b, int returns) {
		return gte(returns, 0);
	}

	@Condition
	public static boolean result_divides_arguments(int a, int b, int returns) {
		return divides(a, returns) & divides(b, returns);
	}

	@Require("arguments_are_positive")
	@Ensure("result_is_positive")
	@Ensure("result_divides_arguments")
	public static int gcd(int a, int b) {
		final int result;
		if (a == b) {
			return a;
		} else if (a > b) {
			result = gcd(a - b, b);
			return result;
		} else {
			result = gcd(a, b - a);
			return result;
		}
	}

}
