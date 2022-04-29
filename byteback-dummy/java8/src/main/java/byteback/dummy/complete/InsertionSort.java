package byteback.dummy.complete;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Quantifier.*;
import static byteback.annotations.Special.*;

import byteback.annotations.Binding;
import byteback.annotations.Contract.Ensure;

public class InsertionSort {

	@Pure
	public static boolean sorted(final int[] xs, int start, int end) {
		int i = Binding.integer();
		int j = Binding.integer();

		return forall(i,
									forall(j,
												 implies(lte(start, i) & lt(i, j) & lt(j, end),
																 lte(xs[i], xs[j]))));
	}

	@Pure
	public static boolean partitioned(final int[] xs, int i, int j) {
		int m = Binding.integer();
		int n = Binding.integer();

		return forall(m,
									forall(n,
												 implies(lte(0, m) & lt(m, j) & lt(j, n) & lte(n, i),
																 lte(xs[m], xs[n]))));
	}

	@Pure
	@Condition
	public static boolean sorted(final int[] xs) {
		return sorted(xs, 0, xs.length);
	}

	@Condition
	public static boolean swapped(final int[] xs, int i, int j) {
		return eq(xs[i], old(xs[j])) & eq(xs[j], old(xs[i]));
	}

	@Condition
	public static boolean bounded_indexes(final int[] xs, int i, int j) {
		return gte(i, 0) & gte(j, 0) & lt(i, xs.length) & lt(j, xs.length);
	}

	@Condition
	public static boolean near_indexes(final int[] xs, int i, int j) {
		return eq(i, j + 1);
	}

	@Condition
	public static boolean array_is_not_null(final int[] xs, int i, int j) {
		return array_is_not_null(xs);
	}

	@Pure
	@Condition
	public static boolean array_is_not_null(final int[] xs) {
		return neq(xs, null);
	}

	@Condition
	public static boolean array_invariance(final int[] xs, int i, int j) {
		int m = Binding.integer();
		return forall(m, implies(lte(0, m) & lt(m, xs.length) & neq(m, i) & neq(m, j), eq(old(xs[m]), xs[m])));
	}

	@Condition
	public static boolean multi_element_array(final int[] xs) {
		return gt(xs.length, 1);
	}

	@Require("array_is_not_null")
	@Require("multi_element_array")
	@Ensure("sorted")
	public static void sort(final int[] xs) {
		for (int i = 1; i < xs.length; ++i) {
			invariant(lte(1, i));
			invariant(lte(i, xs.length));
			invariant(sorted(xs, 0, i));

			for (int j = i; j > 0 && xs[j - 1] > xs[j]; --j) {
				invariant(lte(1, i));
				invariant(lt(i, xs.length));
				invariant(lte(0, j));
				invariant(lte(j, i));
				invariant(partitioned(xs, i, j));
				invariant(sorted(xs, j, i + 1));

				assertion(lte(xs[j], xs[j - 1]));
				swap(xs, j, j - 1);
				assertion(lte(xs[j - 1], xs[j]));
			}
		}
	}

	@Require("near_indexes")
	@Require("array_is_not_null")
	@Require("bounded_indexes")
	@Ensure("swapped")
	@Ensure("array_invariance")
	public static void swap(final int[] xs, int i, int j) {
		final int y;
		y = xs[i];
		xs[i] = xs[j];
		xs[j] = y;
	}

}
