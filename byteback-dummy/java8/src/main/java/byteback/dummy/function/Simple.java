package byteback.dummy.function;

import static byteback.annotations.Quantifier.exists;
import static byteback.annotations.Quantifier.forall;

import byteback.annotations.Binding;
import byteback.annotations.Contract.Pure;

public class Simple {

	@Pure
	public static boolean universalQuantifier() {
		int i = Binding.INTEGER();

		return forall(i, true);
	}

	@Pure
	public static boolean existentialQuantifier() {
		int i = Binding.INTEGER();

		return exists(i, true);
	}

}
