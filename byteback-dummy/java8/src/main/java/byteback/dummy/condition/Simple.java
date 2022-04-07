package byteback.dummy.condition;

import static byteback.annotations.Contract.assertion;
import static byteback.annotations.Contract.assumption;
import static byteback.annotations.Contract.invariant;
import static byteback.annotations.Operator.eq;
import static byteback.annotations.Operator.gt;

import byteback.annotations.Contract.Condition;
import byteback.annotations.Contract.Ensure;
import byteback.annotations.Contract.Require;

public class Simple {

	@Condition
	public static boolean returns_1(int returns) {
		return eq(returns, 1);
	}

	@Condition
	public static boolean returns_argument(int a, int returns) {
		return eq(returns, a);
	}

	@Condition
	public static boolean argument_is_positive(int m, int returns) {
		return gt(m, 0);
	}

	@Condition
	public static boolean returns_0(int returns) {
		return true;
	}

	@Condition
	public static boolean returns_0(long returns) {
		return false;
	}

	@Ensure("returns_1")
	public static int returnsOne() {
		return 1;
	}

	@Ensure("returns_argument")
	public static int identity(int a) {
		return a;
	}

	public static void singleAssertion() {
		assertion(true);
	}

	public static void singleAssumption() {
		assumption(true);
	}

	public static void wrongAssumption1() {
		assumption(false);
	}

	public static void wrongAssumption2() {
		boolean a = false;

		assumption(a);
	}

	@Require("argument_is_positive")
	public static int fibonacci(int m) {
		int a = 0;
		int b = 1;
		int c;

		for (int i = 0; i < m; ++i) {
			c = a + b;
			a = b;
			b = c;
		}

		return a;
	}

	@Ensure("returns_0")
	public static int overloadedConditions() {
		return 0;
	}

	@Ensure("returns_1")
	public static int result() {
		return 1;
	}

	@Ensure("returns_1")
	public static int returnsResult() {
		return result();
	}

	public static void loopAssertion() {
		int c = 0;

		for (int i = 0; i < 10; ++i) {
			assertion(eq(c, 0));
		}
	}

	public static void loopInvariant() {
		int c = 0;

		for (int i = 0; i < 10; ++i) {
			invariant(eq(c, 0));
		}
	}

}
