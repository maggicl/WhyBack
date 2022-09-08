/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.controlflow;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Quantifier.*;

import byteback.annotations.Binding;
import byteback.annotations.Contract.Pure;

public class EnhancedFor {

	@Pure
	public static boolean contains(int[] a, int x) {
		final int i = Binding.integer();

		return exists(i, lte(0, i) & lt(0, a.length) & eq(a[i], x));
	}

	public static void forEach(int[] a) {
		for (int x : a) {
			assertion(contains(a, x));
		}
	}

}
/**
 * RUN: %{verify} %t.bpl | filecheck %s CHECK: Boogie program verifier finished
 * with 2 verified, 0 errors
 */
