/**
 * RUN: %{translate} %{byteback} %{jar} %s %t
 */
package byteback.test.casting;

public class BoolToInt {

	boolean t;

	public int implicit() {
		int i = 0;

		if (t) {
			i = 1;
		} else {
			i = 2;
		}

		return i;
	}

}
/**
 * RUN: %{verify} %t
 * CHECK-IGNORE: Boogie program verifier finished with 2 verified, 0 errors
 */
