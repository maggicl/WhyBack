/**
 * RUN: %{translate} %{byteback} %{jar} %s %t
 */
package byteback.test.casting;

public class RealToInt {

	public int explicit() {
		float f = 3.14f;
		double d = 3.14d;
		int i = (int)f;
		i = (int)d;

		return i;
	}

}
/**
 * RUN: %{verify} %t.mlw
 * CHECK-IGNORE: Boogie program verifier finished with 2 verified, 0 errors
 */
