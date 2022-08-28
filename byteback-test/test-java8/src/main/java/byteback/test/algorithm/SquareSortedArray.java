package byteback.test.algorithm;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Quantifier.*;
import byteback.annotations.Binding;

public class SquareSortedArray {

	@Pure
	public static boolean sorted(final int[] a, final int i, final int j) {
		final int k = Binding.integer();

		return forall(k, implies(lt(i, k) & lt(k, j), lte(a[k - 1], a[k])));
	}

	@Predicate
	public static boolean array_is_sorted(final int[] a) {
		return sorted(a, 0, a.length);
	}

	@Predicate
	public static boolean array_is_sorted(final int[] a, final int[] b) {
		return sorted(b, 0, b.length);
	}

	@Require("array_is_sorted")
	@Ensure("array_is_sorted")
	public static int[] square_sorted_array(final int[] a) {
		final int[] b = new int[a.length];

		int i = 0;
		int j = a.length - 1;
		int c = a.length - 1;

		while (i <= j) {
			int iq = a[i] * a[i];
			int jq = a[j] * a[j];

			if (iq < jq) {
				b[c] = jq;
				j -= 1;
			} else {
				b[c] = iq;
				i += 1;
			}

			c -= 1;
		}

		return b;
	}
	
}
