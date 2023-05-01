/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.exceptions;

import static byteback.annotations.Contract.*;

import byteback.test.exceptions.Basic.Exception1;
import byteback.test.exceptions.Basic.Exception2;

public class MultiCatch {

	@Predicate
	public boolean always_throws_exception1_exception2(Throwable e) {
		return e instanceof Exception1 | e instanceof Exception2;
	}

	@Ensure("always_throws_exception1_exception2")
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

	// TODO: Discuss this
	// public void multiCatchFinally() throws Exception {
	// 	try {
	// 		alwaysThrowsMultiple();
	// 		assertion(false);
	// 	} catch (Exception3 | Exception4 e) {
	// 		assertion(false);
	// 	} finally {
	// 	}
	// }

}
/**
 * RUN: %{verify} %t.bpl | filecheck %s
 * CHECK: Boogie program verifier finished with 6 verified
 */
