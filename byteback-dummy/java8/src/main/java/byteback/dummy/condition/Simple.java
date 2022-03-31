package byteback.dummy.condition;

import static byteback.annotations.Operator.eq;

import byteback.annotations.Contract.Condition;
import byteback.annotations.Contract.Ensure;
import static byteback.annotations.Contract.assertion;
import static byteback.annotations.Contract.assumption;

public class Simple {

	@Condition
	public static boolean returns_1(int returns) {
		return eq(returns, 1);
	}

	@Condition
	public static boolean returns_argument(int a, int returns) {
		return eq(returns, a);
	}

	@Ensure("returns_1")
	public static int returnsOne() {
		return 1;
	}

	@Ensure("returns_argument")
	public static int identity(int a) {
		return a;
	}

  public static void singleAssertion() {
    assertion(true);
  }

  public static void singleAssumption() {
    assumption(true);
  }

  public static void wrongAssumption1() {
    assumption(false);
  }

  public static void wrongAssumption2() {
    boolean a = false;

    assumption(a);
  }

}
