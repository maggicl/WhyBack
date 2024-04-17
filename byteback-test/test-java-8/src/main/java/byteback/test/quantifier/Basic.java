/**
 * RUN: %{translate} %{byteback} %{jar} %s %t
 */
package byteback.test.quantifier;

import static byteback.annotations.Quantifier.exists;
import static byteback.annotations.Quantifier.forall;
import static byteback.annotations.Operator.*;

import byteback.annotations.Binding;
import byteback.annotations.Contract.Pure;

public class Basic {

	@Pure
	public static boolean universalQuantifier() {
		int i = Binding.integer();

		return forall(i, eq(i, 0));
	}

	@Pure
	public static boolean existentialQuantifier() {
		int i = Binding.integer();

		return exists(i, eq(i, 0));
	}

}
/**
 * RUN: %{verify} %t
 * CHECK-IGNORE: Boogie program verifier finished with 1 verified, 0 errors
 */
