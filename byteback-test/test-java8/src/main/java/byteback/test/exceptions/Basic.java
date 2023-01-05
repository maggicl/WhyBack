/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.exceptions;

import static byteback.annotations.Contract.*;

public class Basic {

	public Exception tryCatchBlock() {
		try {
			throw new Exception();
		} catch (final Exception e) {
			return e;
		}
	}

	public void alwaysThrows() throws Exception {
		throw new Exception();
	}

	public void catches() {
		try {
			alwaysThrows();
		} catch (Exception e) {
			assertion(false);
		}
	}

}
/**
 * RUN: %{verify} %t.bpl | filecheck %s
 * CHECK: Boogie program verifier finished with 2 verified, 0 errors
 */
