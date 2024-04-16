/**
 * RUN: %{byteback} -cp %{jar} -c byteback.test.instance.Unit -o %t.mlw
 */
package byteback.test.instance;

public class Unit {

	public static <T> T identity(T argument) {
		return argument;
	}

}
/**
 * RUN: %{verify} %t.mlw
 * CHECK-IGNORE: Boogie program verifier finished with 2 verified, 0 errors
 */
