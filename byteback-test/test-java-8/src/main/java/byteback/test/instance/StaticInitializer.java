/**
 * RUN: %{translate} %{byteback} %{jar} %s %t
 */
package byteback.test.instance;

public class StaticInitializer {

	static final int i;

	static {
		i = 1;
	}

}
/**
 * RUN: %{verify} %t
 * CHECK-IGNORE: Boogie program verifier finished with 2 verified, 0 errors
 */
