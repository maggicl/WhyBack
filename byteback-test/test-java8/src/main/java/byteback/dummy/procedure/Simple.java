package byteback.dummy.procedure;

@SuppressWarnings("unused")
public class Simple {

	public static void empty() {
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

	public static void emptyFor() {
		for (int i = 0; i < 10; ++i) {
		}
	}

	public static void emptyNestedFor() {
		for (int i = 0; i < 10; ++i) {
			for (int j = 0; j < 10; ++j) {
			}
		}
	}

	public static Object returnsNull() {
		return null;
	}

	public static void realCondition() {
		double r = 3.14;

		if (r < 2.72) {
		}
	}

	public static void assignsProcedureResult() {
		Object a = returnsNull();
	}

	public static void assignsProcedureResultTwice() {
		Object a = returnsNull();
		a = returnsNull();
	}

	public static void callsVoidProcedure() {
		emptyWhile();
	}

	public static void callsInForLoop() {
		for (int i = 0; i < 10; ++i) {
			emptyFor();
		}
	}

}
