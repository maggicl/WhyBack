package byteback.dummy.condition;

import byteback.annotations.Quantifier;
import byteback.annotations.Contract.Condition;
import byteback.annotations.Contract.Ensure;

import static byteback.annotations.Quantifier.forall;
import static byteback.annotations.Operator.lt;
import static byteback.annotations.Operator.eq;
import static byteback.annotations.Operator.gte;
import static byteback.annotations.Operator.and;
import static byteback.annotations.Operator.implies;

public class Array {

  @Condition
  public static boolean sum_of_positive_integers_is_positive(int[] as, int returns) {
    int index = Quantifier.INTEGER;
    return implies(forall(index, and(lt(index, as.length), gte(index, 0))), gte(returns, 0));
  }

  @Condition
  public static boolean last_element_is_1(int[] as) {
    return eq(as[as.length - 1], 1);
  }

  @Ensure("sum_of_positive_integers_is_positive")
	public static int sum(int[] as) {
		int c = 0;

		for (int a : as) {
			c += a;
		}

		return c;
	}

  @Ensure("last_element_is_1")
	public static void assignsLastElement(int[] as) {
		as[as.length - 1] = 1;
	}

}
