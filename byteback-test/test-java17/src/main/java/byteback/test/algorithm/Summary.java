/**
 * RUN: %{byteback} -cp %{jar} -c %{class} | tee %t.bpl
 */
package byteback.test.algorithm;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;

public class Summary {

	static int summary(int... values) {
		var result = 0;
		for (var v : values)
			result += switch (v) {
				case 0:
					yield (1);
				case 1:
					yield (-1);
				default:
					if (v > 0)
						yield (v);
					else
						yield (0);
			};
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
