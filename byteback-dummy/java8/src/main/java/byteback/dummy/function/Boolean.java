package byteback.dummy.function;

import byteback.annotations.Contract.Pure;

public class Boolean {

	@Pure
	public static boolean or(boolean a, boolean b) {
		return a | b;
	}

	@Pure
	public static boolean and(boolean a, boolean b) {
		return a & b;
	}

	@Pure
	public static boolean xor(boolean a, boolean b) {
		return a ^ b;
	}

	@Pure
	public static boolean returnsTrue() {
		return true;
	}

	@Pure
	public static boolean returnsFalse() {
		return false;
	}

}
