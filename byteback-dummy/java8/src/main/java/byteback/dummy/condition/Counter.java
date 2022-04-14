package byteback.dummy.condition;

import static byteback.annotations.Contract.assertion;
import static byteback.annotations.Operator.eq;
import static byteback.annotations.Special.old;

import byteback.annotations.Contract.Condition;
import byteback.annotations.Contract.Ensure;

public class Counter {

  @Condition
  public boolean increments_count_by_1() {
    return eq(count, old(count) + 1);
  }

  @Condition
  public boolean increments_count_by_10() {
    return eq(count, old(count) + 10);
  }

	public static void main() {
		final Counter counter = new Counter();
		counter.increment();
    assertion(eq(counter.count, 0));
    counter.countTo10();
    assertion(eq(counter.count, 11));
		counter.countTo10Indirectly();
    assertion(eq(counter.count, 21));
	}

	int count;

	public Counter() {
		this.count = 0;
	}

  @Ensure("increments_count_by_1")
	public void increment() {
		count++;
	}

  @Ensure("increments_count_by_10")
	public void countTo10() {
		for (int i = 0; i < 10; ++i) {
			count++;
		}
	}

  @Ensure("increments_count_by_10")
	public void countTo10Indirectly() {
		for (int i = 0; i < 10; ++i) {
			increment();
		}
	}

}
