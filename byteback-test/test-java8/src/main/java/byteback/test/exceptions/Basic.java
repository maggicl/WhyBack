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

	public void neverThrows() throws Exception {
	}

	public void catches() {
		try {
			neverThrows();
		} catch (Exception e) {
			assertion(false);
		}
	}

}
/**
 * RUN: %{verify} %t.bpl
 */
