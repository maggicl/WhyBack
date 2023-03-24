/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.exceptions;

import static byteback.annotations.Contract.*;

public class Multicatch {

	public static class Exception1 extends Exception {

		@Return
		public Exception1() {}

	}

	public static class Exception2 extends Exception {

		@Return
		public Exception2() {}

	}

	@Predicate
	public boolean always_throws_exception1_exception2(Throwable e) {
		return e instanceof Exception1 | e instanceof Exception2;
	}

	@Ensure("always_throws_exception1_exception2")
	public void alwaysThrowsMultiple() throws Exception1, Exception2 {
		throw new Exception1();
	}

	public void multiCatch() {
		try {
			alwaysThrowsMultiple();
			assertion(false);
		} catch (Exception1 | Exception2 e) {
			assertion(e instanceof Exception1 | e instanceof Exception2);
		}
	}
}
/**
 * RUN: %{verify} %t.bpl
 * CHECK: Boogie program verifier finished with 5 verified
 */
