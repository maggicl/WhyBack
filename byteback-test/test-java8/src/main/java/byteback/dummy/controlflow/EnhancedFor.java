package byteback.dummy.controlflow;

import byteback.annotations.Binding;
import byteback.annotations.Contract.Pure;
import static byteback.annotations.Quantifier.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Contract.*;

public class EnhancedFor {

	@Pure
	public static boolean contains(int[] a, int x) {
		int i = Binding.integer();

		return exists(i, lte(0, i) & lt(0, a.length) & eq(a[i], x));
	}

	public static void forEach(int[] a) {
		for (int x : a) {
			invariant(contains(a, x));
		}
	}
	
}
