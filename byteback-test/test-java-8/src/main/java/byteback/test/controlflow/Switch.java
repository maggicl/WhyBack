/**
 * RUN: %{byteback} -cp %{jar} -c byteback.test.controlflow.Switch -o %t.mlw
 */
package byteback.test.controlflow;

public class Switch {

	public static int intSwitch(final int a) {
		int b;

		switch (a) {
			case 1 :
				b = 1;
			case 2 :
				b = 2;
				break;
			default :
				b = 0;
		}

		return b;
	}

}
/**
 * RUN-IGNORE: %{verify} %t.bpl | filecheck %s
 * CHECK-IGNORE: Boogie program verifier finished with 2 verified, 0 errors
 */
