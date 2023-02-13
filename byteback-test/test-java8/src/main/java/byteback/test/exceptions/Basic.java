/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.exceptions;

import static byteback.annotations.Operator.*;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import static byteback.annotations.Contract.*;

public class Basic {

	public Exception tryCatchBlock() {
		try {
			throw new Exception();
		} catch (final Exception e) {
			return e;
		}
	}

	@Return
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

	@Predicate
	public boolean throws_iff_even(final int n, final Exception e) {
		return implies(neq(n % 2, 0), eq(e, null));
	}

	@Raise(exception = Exception.class, when = "argument_is_even")
	@Ensure("throws_iff_even")
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
	}

	public void tryWithResources() throws IOException {
		try (final FileInputStream input = new FileInputStream("file.txt")) {
			"test".replace('a', 'b');
		}
	}

}
/**
 * RUN: %{verify} %t.bpl
 */
