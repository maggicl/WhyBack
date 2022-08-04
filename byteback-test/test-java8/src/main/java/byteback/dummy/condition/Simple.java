package byteback.dummy.condition;

import static byteback.annotations.Contract.assertion;
import static byteback.annotations.Contract.assumption;
import static byteback.annotations.Contract.invariant;
import static byteback.annotations.Operator.eq;
import static byteback.annotations.Operator.gt;
import static byteback.annotations.Operator.implies;
import static byteback.annotations.Operator.lte;
import static byteback.annotations.Special.conditional;

import byteback.annotations.Contract.Ensure;
import byteback.annotations.Contract.Predicate;
import byteback.annotations.Contract.Pure;
import byteback.annotations.Contract.Require;

public class Simple {

	@Predicate
	public static boolean returns_1(int returns) {
		return eq(returns, 1);
	}

	@Predicate
	public static boolean returns_argument(int a, int returns) {
		return eq(returns, a);
	}

	@Predicate
	public static boolean argument_is_positive(int m, int returns) {
		return gt(m, 0);
	}

	@Predicate
	public static boolean argument_is_positive(int m) {
		return gt(m, 0);
	}

	@Predicate
	public static boolean returns_0(int returns) {
		return true;
	}

	@Predicate
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

	@Pure
	public static int recursive_fibonacci(int m) {
		return conditional(lte(m, 0), 0, recursive_fibonacci(m));
	}

	@Require("argument_is_positive")
	public static int fibonacci(int m) {
		int a = 0;
		int b = 1;
		int c;

		for (int i = 0; i < m; ++i) {
			invariant(lte(i, m));
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
			int f = 0;
			invariant(eq(f, c));
		}
	}

	public static void assignIf(int b) {
		int a = 0;

		if (a < b) {
			a = 1;
		} else {
			a = 2;
		}

		assertion(implies(gt(b, a), eq(a, 1)));
		assertion(gt(a, 0));
	}

	public static void fizz() {
	}

	public static void buzz() {
	}

	@Require("argument_is_positive")
	public static void fizzBuzz(int n) {
		for (int i = 0; i < n; ++i) {
			invariant(lte(i, n));

			if (i % 3 == 0) {
				fizz();
			}

			if (i % 5 == 0) {
				buzz();
			}
		}
	}

}
