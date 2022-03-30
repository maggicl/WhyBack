package byteback.frontend.boogie;

import byteback.frontend.boogie.ast.PrintUtil;
import byteback.frontend.boogie.ast.Printable;

public class TestUtil {

  public static boolean astEquals(final Printable a, final Printable b) {
    return PrintUtil.toString(a).equals(PrintUtil.toString(b));
  }

  public static void assertAstEquals(final Printable expected, final Printable actual) {
    if (!astEquals(expected, actual)) {
      System.err.println("EXPECTED:");
      System.err.println(PrintUtil.toString(expected));
      System.err.println("ACTUAL:");
      System.err.println(PrintUtil.toString(actual));
    }
  }
  
}
