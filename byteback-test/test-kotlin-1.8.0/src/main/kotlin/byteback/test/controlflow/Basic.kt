/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */

package byteback.test.controlflow

class Basic {

		fun sum(a: Int, b: Int): Int {
				return a + b
		}

		fun empty(): Unit {
		}

		fun doubleAssignment(): Unit {
				var a = 0
				a = a + 42
		}

		fun emptyWhile(a: Boolean): Unit {
				// empty while is optimized out
				while (a) {
				}
		}

		fun emptyDoWhile(a: Boolean): Unit {
				// empty do-while is optimized out
				do {
				} while (a);
		}

		fun emptyIf(a: Boolean): Unit {
				if (a) {
				}
		}

}

/**
 * RUN: %{verify} %t.bpl | filecheck %s
 * CHECK: Boogie program verifier finished with 7 verified, 0 errors
 */
