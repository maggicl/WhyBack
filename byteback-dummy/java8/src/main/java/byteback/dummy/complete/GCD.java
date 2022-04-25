package byteback.dummy.complete;

import byteback.annotations.Contract.Ensure;
import byteback.annotations.Contract.Require;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Contract.*;

public class GCD {

	@Condition
	public static boolean arguments_are_positive(int a, int b, int returns) {
		return gt(a, 0) & gt(b, 0);
	}

	@Condition
	public static boolean result_is_positive(int a, int b, int returns) {
		return gte(returns, 0);
	}

	@Require("arguments_are_positive")
	@Ensure("result_is_positive")
	public static int gcd(int a, int b) {
		return 1;
	}

}
