/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.controlflow

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;

class Basic {
		public static void empty() {
		}

		public static void doubleAssignment() {
				int a = 0
				a = a + 42
		}

		public static int returns1() {
				return 1
		}
}
