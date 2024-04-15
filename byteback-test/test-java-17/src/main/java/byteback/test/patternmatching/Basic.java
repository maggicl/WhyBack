/**
 * RUN: %{byteback} -cp %{jar} -c byteback.test.patternmatching.Basic -o %t.mlw
 */
package byteback.test.patternmatching;

public class Basic {

	public static int test() {
		Object obj = new int[10];

		if (obj instanceof int[] matched) {
			int len = matched.length;

			return len;
		}

		return 0;
	}

}
/**
 * RUN-IGNORE: %{verify} %t.bpl | filecheck %s
 * CHECK-IGNORE: Boogie program verifier finished with 2 verified, 0 errors
 */
