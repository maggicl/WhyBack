package byteback.dummy.procedure;

public class Simple {

	public static void empty() {
	}

	public static void singleAssignment() {
		int a = 42;
	}

	public static void doubleAssignment() {
		int a = 0;
		a = a + 42;
	}

	public static void emptyWhile() {
		boolean a = false;

		while (a) {
		}
	}

	public static void emptyDoWhile() {
		boolean a = false;

		do {
		} while (a);
	}

	public static void emptyIf() {
		boolean a = false;

		if (a) {
		}
	}

	public static void assignIf() {
		boolean a = false;

		if (!a) {
			a = true;
		}
	}

	public static void assignParameter(int a) {
		a = 1;
	}

}
