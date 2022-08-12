package byteback.test.complete;

import byteback.annotations.Contract._;
import byteback.annotations.Special._;
import byteback.annotations.Operator._;
import byteback.annotations.Operator.{eq => equal};

class Counter() {
  var count: Int = 0;

  @Predicate
  def increments_count_by_1(): Boolean = {
		return equal(count, old(count) + 1);
  }

  @Predicate
  def increments_count_by_10(): Boolean = {
		return equal(count, old(count) + 10);
  }

  @Ensure("increments_count_by_1")
  def increment() : Unit = {
    count = count + 1;
  }

  @Ensure("increments_count_by_10")
  def incrementTo10(): Unit = {
    var i: Int = 0;
		var old_count: Int = count;

    while (i < 10) {
			invariant(lte(0, i) & lte(i, 10));
			invariant(eq(count, old_count + i));

      increment();
      i = i + 1;
    }
  }

}
