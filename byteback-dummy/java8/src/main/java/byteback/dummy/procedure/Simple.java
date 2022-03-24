package byteback.dummy.procedure;

public class Simple {

	public void empty() {
	}

	public void singleAssignment() {
		int a = 1;
	}

	public void doubleAssignment() {
		int a = 0;
		a = a + 42;
	}

	public void emptyDoWhile() {
		boolean a = false;

		do {
		} while (a);
	}

	public void emptyIf() {
		boolean a = false;

		if (a) {
		}
	}

	public void assignIf() {
		boolean a = false;

		if (!a) {
			a = true;
		}
	}

	public void shortCircuitingAnd() {
		boolean a = true;
		boolean b = true;
		boolean c = a && b;
	}

	public void shortCircuitingOr() {
		boolean a = true;
		boolean b = true;
		boolean c = a || b;
	}

	public void shortCircuitingNot() {
		boolean a = true;
		boolean c = !a;
	}

}
