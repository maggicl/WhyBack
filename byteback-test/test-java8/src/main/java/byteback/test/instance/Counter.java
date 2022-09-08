/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.instance;

public class Counter {

	public static void main() {
		final Counter counter = new Counter();
		counter.increment();
		counter.countTo10();
		counter.countTo10Indirectly();
	}

	int count;

	public Counter() {
		this.count = 0;
	}

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
/**
 * RUN: %{verify} %t.bpl | filecheck %s
 * CHECK: Boogie program verifier finished with 5 verified, 0 errors
 */
