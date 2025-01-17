/**
 * RUN: %{translate} %{byteback} %{jar} %s %t
 */
package byteback.test.instance;

import byteback.annotations.Contract._;
import byteback.annotations.Special._;
import byteback.annotations.Operator._;
import byteback.annotations.Operator.{eq => equal};

abstract class Die {

  @Pure
  @Predicate
  def outcome_is_positive(max: Int, outcome: Int): Boolean = {
    return lte(1, outcome);
  }

  @Pure
  @Predicate
  def outcome_is_leq_than_max(max: Int, outcome: Int): Boolean = {
    return lte(outcome, max);
  }

  @Ensure("outcome_is_positive")
  @Ensure("outcome_is_leq_than_max")
  def roll(max: Int): Int

}

class FixedDie extends Die {

  @Pure
  @Predicate
  def result_is_max(max: Int, returns: Int): Boolean = {
    return equal(max, returns);
  }

  @Ensure("result_is_max")
  def roll(max: Int): Int = max;

}

class Dice {

  def main(): Unit = {
    var die: FixedDie = new FixedDie();
    var max: Int = 6;
    var result: Int = die.roll(max);

    assertion(lte(result, max));
  }

}
/**
 * RUN: %{verify} %t
 * CHECK-IGNORE: Boogie program verifier finished with 5 verified, 0 errors
 */
