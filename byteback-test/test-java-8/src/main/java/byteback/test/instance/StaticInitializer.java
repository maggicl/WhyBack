/**
 * RUN: %{byteback} -cp %{jar} -c byteback.test.instance.StaticInitializer -o %t.mlw
 */
package byteback.test.instance;

public class StaticInitializer {

	static final int i;

	static {
		i = 1;
	}

}
/**
 * RUN-IGNORE: %{verify} %t.bpl | filecheck %s
 * CHECK-IGNORE: Boogie program verifier finished with 2 verified, 0 errors
 */
