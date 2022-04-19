package byteback.annotations;

public interface Binding {

	public static int INTEGER() {
		return 0;
	}

	public static double REAL() {
		return 0.0;
	}

	public static Object REFERENCE() {
		return null;
	}

	public static boolean BOOLEAN() {
		return false;
	}

}
