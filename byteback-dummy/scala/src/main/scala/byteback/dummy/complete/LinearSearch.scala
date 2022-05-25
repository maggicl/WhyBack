package byteback.dummy.complete;

import byteback.annotations.Contract._;
import byteback.annotations.Operator._;
import byteback.annotations.Operator.{eq => equal};

class LinearSearch {

  @Condition
  def bounded_indices(a: Array[Int], n: Int, left: Int, right: Int): Boolean = {
    return lte(0, left) & lte(left, right) & lte(right, a.length);
  }

  @Condition
  def result_is_index(a: Array[Int], n: Int, left: Int, right: Int, returns: Int): Boolean = {
    return implies(lte(0, returns), equal(a(returns), n));
  }

  @Require("bounded_indices")
  @Ensure("result_is_index")
  def apply(a: Array[Int], n: Int, left: Int, right: Int): Int = {
    var i: Int = left;

    while (i < right) {
			invariant(lte(left, i) & lte(i, right));

      if (a(i) == n) {
        return i;
      }

      i = i + 1;
    }

    return -1;
  }

}
