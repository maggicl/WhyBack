/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl 2>&1
 */
package byteback.test.controlflow;

public class Switch {

	/**
	 * CHECK: procedure {{.*}}.intSwitch#int#
	 */
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
 * {%verify} %t.bpl | filecheck %s
 * CHECK: Verified
 */
