/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.casting;

public class BoolToInt {

	public void implicit() {
		boolean c = false;
		int i = 0;
		if (c) {
			i = 1;
		} else {
			i = 2;
		}
	}

}
/**
 * RUN: %{verify} %t.bpl | filecheck %s CHECK: Boogie program verifier finished
 * with 2 verified, 0 errors
 */
