/**
 * RUN: %{byteback} -cp %{jar} -c %{class} | tee %t.bpl
 */
package byteback.test.controlflow;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;

public class SwitchExpression {

	public static int state = 1;

	public static void switchInteger() {
		state = 1;
		int a = switch(state) { case 1 -> 1; case 2 -> 2; default -> 3; };

		assertion(eq(a, 1));
	}

	public static void switchYieldInteger() {
		state = 1;
		int a = switch(state) { case 1 -> { yield(1); } case 2 -> { yield(2); } default -> { yield(3); } };

		assertion(eq(a, 1));
	}

}
/**
 * RUN: %{verify} %t.bpl
 */
