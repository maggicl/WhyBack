/**
 * RUN: %{byteback} -cp %{jar} -c %{class} | tee %t.bpl
 */
package byteback.test.algorithm;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Quantifier.*;

import byteback.annotations.Binding;

public class Summary {


	@Pure
	public static boolean contains(int[] as, int e, int from, int to) {
		int i = Binding.integer();

		return exists(i, lte(from, i) & lt(i, to) & eq(as[i], e));
	}


	@Predicate
	public static boolean values_do_not_contain_minus1(int... values) {
		return not(contains(values, -1, 0, values.length));
	}


	@Require("values_do_not_contain_minus1")
	public static int summary(int... values) {
		var result = 0;

		for (var value : values) {
			invariant(gte(result, 0));

			result += switch (value) {
				case 0:
					yield (1);
				case 1:
					yield (-1);
				default:
					if (value > 0)
						yield (value);
					else
						yield (0);
			};
		}

		return result;
	}


	public static void main() {
		int[] a0 = {};
		int[] a1 = { 0 };
		int[] a2 = { 0, 1 };
		int[] a3 = { 0, 1, 2, 3 };
		int[] a4 = { 0, 1, -2, 3 };
		int[] a5 = { 0, -2, 3 };
		int[] a6 = { -4, -2, -3 };
		int[] a7 = { 7, 7, 7 };
		assertion(eq(summary(a0), 0));
		assertion(eq(summary(a1), 1));
		assertion(eq(summary(a2), 0));
		assertion(eq(summary(a3), 5));
		assertion(eq(summary(a4), 3));
		assertion(eq(summary(a5), 4));
		assertion(eq(summary(a6), 0));
		assertion(eq(summary(a7), 21));
	}


}
/**
 * RUN: %{verify} %t.bpl
 */
