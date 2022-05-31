package byteback.dummy.complete;

import byteback.annotations.Contract._;
import byteback.annotations.Special._;
import byteback.annotations.Operator._;
import byteback.annotations.Operator.{eq => equal};

trait Die {

  def outcome_is_positive(max: Int, outcome: Int): Boolean = {
    return lte(1, outcome);
  }

  def outcome_is_leq_than_max(max: Int, outcome: Int): Boolean = {
    return lte(outcome, max);
  }

  @Ensure("outcome_is_positive")
  @Ensure("outcome_is_leq_than_max")
  def roll(max: Int): Int

}

class FaultyDie extends Die {

  def result_is_max(max: Int, returns: Int): Boolean = {
    return equal(max, returns);
  }

  @Ensure("result_is_max")
  def roll(max: Int): Int = max;

}

class Dice {

  def main(): Unit = {
    var die: Die = new FaultyDie();
    var max: Int = 6;
    var result: Int = die.roll(max);

    assertion(lte(result, max));
  }

}
