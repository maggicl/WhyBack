/**
 * RUN: %{byteback} -cp %{jar} -c byteback.test.casting.BoolToInt -o %t.mlw
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
 * RUN: %{verify} %t.mlw
 * CHECK-IGNORE: Boogie program verifier finished with 2 verified, 0 errors
 */
