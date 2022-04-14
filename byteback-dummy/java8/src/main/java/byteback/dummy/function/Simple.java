package byteback.dummy.function;

import static byteback.annotations.Quantifier.exists;
import static byteback.annotations.Quantifier.forall;

import byteback.annotations.Contract.Pure;
import byteback.annotations.Quantifier;

public class Simple {

	@Pure
	public static boolean universalQuantifier() {
		int i = Quantifier.INTEGER;

		return forall(i, true);
	}

	@Pure
	public static boolean existentialQuantifier() {
		int i = Quantifier.INTEGER;

		return exists(i, true);
	}

}
