package byteback.annotations;

/**
 * Utilities to express quantified expressions.
 */
public interface Quantifier {

	public static boolean exists(int $, boolean p) {
		return p;
	}

	public static boolean forall(int $, boolean p) {
		return p;
	}

}
