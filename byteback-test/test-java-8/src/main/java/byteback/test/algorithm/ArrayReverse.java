/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.algorithm;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Quantifier.*;
import static byteback.annotations.Special.*;

import javax.annotation.processing.SupportedSourceVersion;

import byteback.annotations.Binding;

public class ArrayReverse {

	@Pure
	public static boolean reverse_of(final int[] a, final int[] b) {
		int i = Binding.integer();

		return and(eq(a.length, b.length),
							 forall(i, implies(lte(0, i) & lt(i, a.length),
																 eq(a[i], b[b.length - 1 - i]))));
	}

	@Pure
	@Predicate
	public static boolean bounded_index(final int[] a, final int i) {
		return lte(0, i) & lt(i, a.length);
	}

	@Pure
	@Predicate
	public static boolean bounded_indices(final int[] a, final int i, final int j) {
		return bounded_index(a, i) & bounded_index(a, j);
	}

	@Predicate
	public static boolean swapped_elements(final int[] a, final int i, final int j) {
		return eq(old(a[i]), a[j]) & eq(old(a[j]), a[i]);
	}

	@Predicate
	public static boolean invariant_elements(final int[] a, final int i, final int j) {
		int m = Binding.integer();

		return forall(m, implies(lte(0, m) & lt(m, i) | lt(j, m) & lt(m, a.length), eq(a[i], old(a[i]))));
	}

	@Return
	@Require("bounded_indices")
	@Ensure("swapped_elements")
	@Ensure("invariant_elements")
	public static void swap(final int[] a, int i, int j) {
		final int y = a[i];
		a[i] = a[j];
		a[j] = y;
	}

	@Pure
	@Predicate
	public static boolean array_is_null(int[] a) {
		return eq(a, null);
	}

	@Predicate
	public static boolean reversed(int[] a) {
		return implies(not(array_is_null(a)), reverse_of(a, old(a)));
	}

	@Raise(exception = IllegalArgumentException.class, when = "array_is_null")
	@Ensure("reversed")
	public static void reverse(int[] a) {
		if (a == null) {
			throw new IllegalArgumentException("Input array cannot be null");
		}

		final int l = a.length - 1;
		int i = 0;

		while (i < (l - i)) {
			invariant(lte(0, i) & lte(i, (l + 1) / 2));
			swap(a, i, l - i);
		}
	}

}

/**
 * RUN: %{verify} %t.bpl | filecheck %s
 * CHECK: Boogie program verifier finished with 3 verified, 0 errors
 */
