/**
 * RUN: %{byteback} -cp %{jar} -c byteback.test.exceptions.MultiCatch -o %t.mlw
 */
package byteback.test.exceptions;

import static byteback.annotations.Contract.*;

import byteback.test.exceptions.Basic.Exception4;

public class MultiCatch {

	@Predicate
	public boolean always_throws_exception1_exception2(final Throwable e) {
		return e instanceof Exception1;
	}

	// TODO: re-enable with test when exception postconditions will be supported. Not supported now due to WhyML native
	//  exception handling limitations
//	@Ensure("always_throws_exception1_exception2")
	public void alwaysThrowsMultiple() throws Exception {
		throw new Exception1();
	}

	public void emptyMulticatch() throws Exception {
		try {
			alwaysThrowsMultiple();
		} catch (Exception1 | Exception2 e) {
		}
	}

	public void multiCatchUnion() throws Exception {
		try {
			alwaysThrowsMultiple();
			assertion(false);
		} catch (Exception1 | Exception2 e) {
			assertion(e instanceof Exception1 | e instanceof Exception2);
		}
	}

	public void multiCatchFinally() throws Exception {
		try {
			alwaysThrowsMultiple();
			assertion(false);
		} catch (Exception3 | Exception4 e) {
			assertion(false);
		} finally {
		}
	}

}
/**
 * RUN: %{verify} %t.mlw
 * CHECK-IGNORE: Boogie program verifier finished with 9 verified
 */
