/**
 * RUN: %{byteback} -cp %{jar} -c byteback.test.casting.RealToInt -o %t.mlw
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
 * RUN-IGNORE: %{verify} %t.bpl | filecheck %s
 * CHECK-IGNORE: Boogie program verifier finished with 2 verified, 0 errors
 */
