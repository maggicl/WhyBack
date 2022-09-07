/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.complete;

import byteback.annotations.Contract._;
import byteback.annotations.Special._;
import byteback.annotations.Operator._;
import byteback.annotations.Operator.{eq => equal};

class DefaultComparator {

  def compare_default(a: Int, b: Int, returns: Boolean): Boolean = {
    return returns;
  }

  @Ensure("compare_default")
  def compare(a: Int, b: Int): Boolean = {
    return true;
  }

}

class LessThanComparator extends DefaultComparator {

  def compare_less_than(a: Int, b: Int, returns: Boolean): Boolean = {
    return implies(returns, lt(a, b));
  }

  @Ensure("compare_less_than")
  override def compare(a: Int, b: Int): Boolean = {
    return a < b;
  }

}


class GreaterThanComparator extends DefaultComparator {

  def compare_greater_than(a: Int, b: Int, returns: Boolean): Boolean = {
    return implies(returns, gt(a, b));
  }

  @Ensure("compare_greater_than")
  override def compare(a: Int, b: Int): Boolean = {
    return a > b;
  }

}

class Comparator {

  def main(): Unit = {
    var ltComparator: LessThanComparator = new LessThanComparator();
    var gtComparator: GreaterThanComparator = new GreaterThanComparator();

    var a: Boolean = ltComparator.compare(2, 1);
    assertion(not(a));

    var b: Boolean = gtComparator.compare(1, 2);
    assertion(not(b));
  }

}
/**
 * RUN: %{verify} %t.bpl | filecheck %s
 * CHECK: Boogie program verifier finished with 11 verified, 0 errors
 */
