/**
 * RUN: %{byteback} -cp %{jar} -c %{class} | tee %t.bpl
 */
package byteback.test.patternmatching;

public class Simple{
	public static void test() {
		Object obj = new int[10];

		if (obj instanceof int[] matched) {
			int len = matched.length;
		}
	}
}
/**
 * RUN: %{verify} %t.bpl
 */
