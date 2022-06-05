package byteback.dummy.condition;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Special.*;

import byteback.annotations.Contract.Ensure;
import byteback.annotations.Contract.Predicate;

public class Counter {

	@Predicate
	public boolean increments_count_by_1() {
		return eq(count, old(count) + 1);
	}

	@Predicate
	public boolean increments_count_by_10() {
		return eq(count, old(count) + 10);
	}

	@Predicate
	public boolean count_is_0() {
		return eq(count, 0);
	}

	public static void main() {
		final Counter counter = new Counter();
		assertion(eq(counter.count, 0));
		counter.increment();
		assertion(eq(counter.count, 1));
		counter.countTo10();
		assertion(eq(counter.count, 11));
		counter.countTo10Indirectly();
		assertion(eq(counter.count, 21));
	}

	int count;

	@Ensure("count_is_0")
	public Counter() {
		this.count = 0;
	}

	@Ensure("increments_count_by_1")
	public void increment() {
		count++;
	}

	@Ensure("increments_count_by_10")
	public void countTo10() {
		final int old_count = count;

		for (int i = 0; i < 10; ++i) {
			invariant(lte(0, i) & lte(i, 10));
			invariant(eq(count, old_count + i));
			count++;
		}
	}

	@Ensure("increments_count_by_10")
	public void countTo10Indirectly() {
		final int old_count = count;

		for (int i = 0; i < 10; ++i) {
			invariant(lte(0, i) & lte(i, 10));
			invariant(eq(count, old_count + i));
			increment();
		}
	}

}
