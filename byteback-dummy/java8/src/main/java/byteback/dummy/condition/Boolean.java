package byteback.dummy.condition;

import static byteback.annotations.Operator.implies;
import static byteback.annotations.Operator.not;

import byteback.annotations.Contract.Condition;
import byteback.annotations.Contract.Ensure;

public class Boolean {

  @Condition
  public static boolean and_postcondition(boolean a, boolean b, boolean returns) {
    return implies(a & b, returns);
  }

  @Condition
  public static boolean or_postcondition(boolean a, boolean b, boolean returns) {
    return implies(a | b, returns);
  }

  @Condition
  public static boolean not_postcondition(boolean a, boolean returns) {
    return implies(a, not(returns));
  }

  @Ensure("and_postcondition")
	public static boolean shortCircuitingAnd(boolean a, boolean b) {
		return a && b;
	}

  @Ensure("or_postcondition")
	public static boolean shortCircuitingOr(boolean a, boolean b) {
		return a || b;
	}

  @Ensure("not_postcondition")
	public static boolean shortCircuitingNot(boolean a) {
		return !a;
	}

}
