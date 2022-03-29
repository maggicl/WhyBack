package byteback.dummy.condition;

import byteback.annotations.Contract.Condition;
import byteback.annotations.Contract.Ensure;

public class Simple {

  @Condition
  public static boolean returns_1(int returns) {
    return returns == 1;
  }

  @Condition
  public static boolean returns_argument(int a, int returns) {
    return returns == a;
  }

  @Ensure("returns_1")
  public static int returnsOne() {
    return 1;
  }

  @Ensure("returns_argument")
  public static int identity(int a) {
    return a;
  }

}
