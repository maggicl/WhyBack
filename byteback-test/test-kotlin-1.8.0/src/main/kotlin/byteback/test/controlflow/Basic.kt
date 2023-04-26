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

		fun emptyWhile(): Unit {
				val a = false
				// empty while is optimized out
				while (a) {
				}
		}

		fun emptyDoWhile(): Unit {
				val a = false
				// empty do-while is optimized out
				do {
				} while (a);
		}

		fun emptyIf(): Unit {
				val a: Boolean = false;

				if (a) {
				}
		}

}

/**
 * RUN: %{verify} %t.bpl | filecheck %s
 * CHECK: Boogie program verifier finished with 7 verified, 0 errors
 */
