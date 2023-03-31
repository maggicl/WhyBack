/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.exceptions;

import static byteback.annotations.Operator.*;
import static byteback.annotations.Contract.*;

public class TryFinally {

	public class Exception1 extends Exception {

		@Return
		public Exception1() {}

	};

	public class Exception2 extends Exception {

		@Return
		public Exception2() {}

	};

	@Predicate
	public boolean always_throws1() {
		return true;
	}

	@Raise(exception = Exception1.class, when = "always_throws1")
	public void alwaysThrows1() throws Exception1, Exception2 {
		throw new Exception1();
	}

	public void finallyBlock() {
		try {
		} finally {
		}
	}

	public void catchFinallyBlock() {
		try {
		} catch (Exception e) {
		}	finally {
		}
	}

	public void finallyIsExecuted() throws Exception {
		try {
			alwaysThrows1();
			assertion(false);
		} catch (Exception2 e) {
			assertion(false);
		} finally {
		}
		assertion(false);
	}

	public void finallyIsExecutedAfterThrowInCatch() throws Exception {
		try {
			alwaysThrows1();
			assertion(false);
		} catch (Exception1 e) {
			alwaysThrows1();
			assertion(false);
		} finally {
		}
		assertion(false);
	}

	@Predicate
	public boolean returns_2(int returns) {
		return eq(returns, 2);
	}

	@Ensure("returns_2")
	@SuppressWarnings("finally")
	public int finallyOverridesReturn() {
		try {
			return 1;
		} finally {
			return 2;
		}
	}

	@Ensure("returns_2")
	@SuppressWarnings("finally")
	public int finallyOverridesThrows() throws Exception {
		try {
			throw new Exception1();
		} finally {
			return 2;
		}
	}

	@Ensure("returns_2")
	@SuppressWarnings("finally")
	public int finallyOverrides1NestedFinally() throws Exception {
		try {
			try {
				throw new Exception1();
			} finally {
				return 1;
			}
		} finally {
			return 2;
		}
	}

	@Ensure("returns_2")
	@SuppressWarnings("finally")
	public int finallyOverrides2NestedFinally() throws Exception {
		try {
			try {
				try {
					throw new Exception1();
				} finally {
					return 3;
				}
			} finally {
				return 1;
			}
		} finally {
			return 2;
		}
	}

}
/**
 * RUN: %{verify} %t.bpl
 */
