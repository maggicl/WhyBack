/**
 * RUN: %{byteback} -cp %{jar} -c byteback.test.instance.Supported -o %t.mlw
 */
package byteback.test.instance;

public class Supported {

	final Unit support;

	public Supported() {
		this.support = new Unit();
	}

}
/**
 * RUN: %{verify} %t.mlw
 * CHECK-IGNORE: Boogie program verifier finished with 3 verified, 0 errors
 */
