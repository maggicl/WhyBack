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
	public void alwaysThrowsMultiple() throws Exception1, Exception2 {
		throw new Exception1();
	}

	public void emptyMulticatch() {
		try {
			alwaysThrowsMultiple();
		} catch (Exception1 | Exception2 e) {
		}
	}

	public void multiCatchUnionAssertion() {
		try {
			alwaysThrowsMultiple();
			assertion(false);
		} catch (Exception1 | Exception2 e) {
			assertion(e instanceof Exception1 | e instanceof Exception2);
		}
	}

}
/**
 * RUN: %{verify} %t.bpl | filecheck %s
 * CHECK: Boogie program verifier finished with 6 verified
 */
