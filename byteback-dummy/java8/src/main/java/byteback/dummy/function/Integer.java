package byteback.dummy.function;

import static byteback.annotations.Operator.eq;
import static byteback.annotations.Operator.not;

import byteback.annotations.Contract.Pure;

public class Integer {

	@Pure
	public static int addition(int a, int b) {
		return a + b;
	}

	@Pure
	public static int subtraction(int a, int b) {
		return a - b;
	}

  @Pure
  public static int minus(int a) {
    return -a;
  }

	@Pure
	public static int multiplication(int a, int b) {
		return a * b;
	}

	@Pure
	public static int division(int a, int b) {
		return a / b;
	}

	@Pure
	public static int modulo(int a, int b) {
		return a % b;
	}

	@Pure
	public static int square(int a) {
		return a * a;
	}

	@Pure
	public static int squareArea(int a) {
		return square(a);
	}

	@Pure
	public static int rectangleArea(int a, int b) {
		return multiplication(a, b);
	}

	@Pure
	public static boolean even(int a) {
		return eq(a % 2, 0);
	}

	@Pure
	public static boolean odd(int a) {
		return not(even(a));
	}


	@Pure
	public static int assignIndirect(int a) {
		int b = a;
		int c = b;
		int d = c;
		int e = d;
		int f = e;

		return f;
	}

	@Pure
	public static int assignPlus(int a) {
		a = a + 1;

		return a;
	}

	@Pure
	public static int nestedPlus(int a) {
		return a + 1 + 2 + 3 + 4 + 5;
	}

	@Pure
	public static int assignPlusIndirectVariables(int a) {
		int b = a + 1;
		int c = b + 2;
		int d = c + 3;
		int e = d + 4;
		int f = e + 5;

		return f;
	}

	@Pure
	public static int commonSubExpressionPlus(int a) {
		a = a + 1;
		int b = a + a;

		return b;
	}

	@Pure
	public static int returnsOne() {
		return 1;
	}

	@Pure
	public static int returnsZero() {
		return 0;
	}

}
