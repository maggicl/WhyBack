/**
 * RUN: %{translate} %{byteback} %{jar} %s %t
 */
package byteback.test.controlflow;

import byteback.annotations.Binding;
import byteback.annotations.Contract.Pure;
import static byteback.annotations.Quantifier.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Contract.*;

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
 * RUN: %{verify} %t
 * CHECK-IGNORE: Boogie program verifier finished with 2 verified, 0 errors
 *
 * /infer:j
 */
