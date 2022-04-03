package byteback.dummy.procedure;

public class Counter {

	int count = 0;

	public void increment() {
		count++;
	}

	public void countTo10() {
		for (int i = 0; i < 10; ++i) {
			count++;
		}
	}

	public void countTo10Indirectly() {
		for (int i = 0; i < 10; ++i) {
			increment();
		}
	}

}
