package byteback.dummy.complete;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Quantifier.*;
import static byteback.annotations.Special.*;

import byteback.annotations.Binding;
import byteback.annotations.Contract.Ensure;

public class InsertionSort {

	@Condition
	public static boolean boundedness(final int[] a, final int i) {
		return lte(0, i) & lt(i, a.length);
	}

	@Condition
	public static boolean boundedness(final int[] a, final int i, final int m) {
		return lte(i, m) & lt(m, a.length);
	}

	@Pure
	public static boolean minimum(final int[] a, final int i, final int j, final int m) {
		final int k = Binding.integer();

		return forall(k, implies(lte(i, k) & lt(k, j), gte(a[k], a[m])));
	}

	@Condition
	public static boolean minimum(final int[] a, final int i, final int m) {
		return minimum(a, i, a.length, m);
	}

	@Require("boundedness")
	@Ensure("boundedness")
	@Ensure("minimum")
	public static int minimum(final int[] a, final int i) {
		int m = i;

		for (int j = i; j < a.length; ++j) {
			invariant(lte(i, j) & lte(j, a.length));
			invariant(lte(i, m) & lt(m, a.length));
			invariant(minimum(a, i, j, m));

			if (a[j] < a[m]) {
				m = j;
			}
		}

		return m;
	}

	@Condition
	public static boolean swapped(final int[] a, final int i, final int j) {
		return eq(old(a[i]), a[j]) & eq(old(a[j]), a[i]);
	}

	@Require("boundedness")
	@Ensure("swapped")
	public static void swap(final int[] a, final int i, final int j) {
		final int y;
		y = a[i];
		a[i] = a[j];
		a[j] = y;
	}

	@Pure
	public static boolean sorted(final int[] a, final int i, final int j) {
		final int k = Binding.integer();

		return forall(k, implies(lte(i, k) & lt(k, j), lte(a[k - 1], a[k])));
	}

	@Pure
	public static boolean partitioned(final int[] a, final int c) {
		final int k = Binding.integer();
		final int l = Binding.integer();

		return forall(k, forall(l, implies(lte(0, k) & lt(k, c) & lte(c, l) & lt(l, a.length), lte(a[k], a[l]))));
	}

	@Condition
	public static boolean sortability(final int[] a) {
		return gt(a.length, 1);
	}

	@Condition
	public static boolean sortedness(final int[] a) {
		return sorted(a, 0, a.length);
	}

	@Require("sortability")
	@Ensure("sortedness")
	public static void sort(final int[] a) {
		for (int c = 0; c < a.length; ++c) {
			invariant(lte(0, c) & lte(c, a.length));
			invariant(partitioned(a, c));
			invariant(sorted(a, 0, c));

			int m = minimum(a, c);

			swap(a, c, m);

			assertion(sorted(a, 0, c + 1));
		}
	}

}
