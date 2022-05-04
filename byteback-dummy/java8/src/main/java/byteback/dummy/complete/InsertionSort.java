package byteback.dummy.complete;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Quantifier.*;
import static byteback.annotations.Special.*;

import byteback.annotations.Binding;

public class InsertionSort {

	@Pure
	public static boolean permutation(final int[] a, final int[] b) {
		final int k = Binding.integer();
		final int m = Binding.integer();

		return forall(k,
				implies(lte(0, k) & lt(k, a.length), exists(m, implies(lte(0, m) & lt(m, b.length), eq(a[k], b[m])))))
				& eq(a.length, b.length);
	}

	@Pure
	public static boolean sorted(final int[] a, final int i, final int j) {
		final int k = Binding.integer();

		return forall(k, implies(lt(i, k) & lt(k, j), lte(a[k - 1], a[k])));
	}

	@Condition
	public static boolean array_is_not_empty(final int[] a) {
		return gt(a.length, 0);
	}

	@Condition
	public static boolean array_is_not_null(final int[] a) {
		return neq(a, null);
	}

	@Condition
	public static boolean array_is_sorted(final int[] a) {
		return sorted(a, 0, a.length);
	}

	@Condition
	public static boolean array_is_permutated(final int[] a) {
		return permutation(a, old(a));
	}

	@Require("array_is_not_null")
	@Require("array_is_not_empty")
	@Ensure("array_is_sorted")
	@Ensure("array_is_permutated")
	public static void sort(final int[] a) {
		final int[] $old = a.clone();

		for (int i = 1; i < a.length; ++i) {
			invariant(lt(0, i) & lte(i, a.length));
			invariant(sorted(a, 0, i));
			invariant(permutation(a, $old));

			for (int j = i; j > 0 && a[j - 1] > a[j]; --j) {
				invariant(lte(0, j) & lte(j, i));
				invariant(sorted(a, 0, j));
				invariant(sorted(a, j, i + 1));
				invariant(permutation(a, $old));

				final int y;

				y = a[j];
				a[j] = a[j - 1];
				a[j - 1] = y;
			}
		}

	}

}