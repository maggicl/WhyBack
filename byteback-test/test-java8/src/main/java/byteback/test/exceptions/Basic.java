/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.exceptions;

import static byteback.annotations.Operator.*;
import static byteback.annotations.Contract.*;

public class Basic {

	public Exception tryCatchBlock() {
		try {
			throw new Exception();
		} catch (final Exception e) {
			return e;
		}
	}

	public void neverThrows() throws Exception {
	}

	public void neverCatches() {
		try {
			neverThrows();
		} catch (Exception e) {
			assertion(false);
		}
	}

	@Predicate
	public boolean always_throws() {
		return true;
	}

	@Raise(exception = Exception.class, when = "always_throws")
	public void alwaysThrows() throws Exception {
		throw new Exception();
	}

	public void alwaysCatches() {
		try {
			alwaysThrows();
			assertion(false);
		} catch (Exception e) {
		}
	}

	@Raise(exception = Exception.class, when = "always_throws")
	public void callsAlwaysThrows() throws Exception {
		alwaysThrows();
	}

	public void finallyIsExecuted() throws Exception {
		boolean f = false;

		try {
			alwaysThrows();
		} catch (Exception e) {
			f = true;
		} finally {
			assertion(f);
		}
	}

	@Predicate
	public boolean argument_is_even(final int n) {
		return eq(n % 2, 0);
	}

	@Raise(exception = Exception.class, when = "argument_is_even")
	public void throwsIfEven(final int n) throws Exception {
		if (n % 2 == 0) {
			throw new Exception();
		}
	}

	public void catchesIfEven() {
		try {
			throwsIfEven(2);
			assertion(false);
		} catch (Exception e) {
		}

		try {
			throwsIfEven(3);
		} catch (Exception e) {
			assertion(false);
		}
	}

}
/**
 * RUN: %{verify} %t.bpl
 */