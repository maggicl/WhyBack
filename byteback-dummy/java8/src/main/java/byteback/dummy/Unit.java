package byteback.dummy;

public class Unit {

	public void voidMethod() {
	}

	public void singleAssignmentMethod() {
		int a = 1;
	}

	public void doubleAssignmentMethod() {
		int a = 0;
		a = a + 42;
	}

	public void emptyDoWhileMethod() {
		boolean a = false;

		do {
		} while (a);
	}

	public void emptyIfMethod() {
		boolean a = false;

		if (a) {
		}
	}

}
