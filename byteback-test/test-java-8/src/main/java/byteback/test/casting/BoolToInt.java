/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.casting;

import static byteback.annotations.Contract.*;

public class BoolToInt {

	boolean t;

	@Return
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
 * RUN: %{verify} %t.bpl | filecheck %s
 * CHECK: Boogie program verifier finished with 2 verified, 0 errors
 */
