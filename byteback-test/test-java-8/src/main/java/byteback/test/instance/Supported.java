/**
 * RUN: %{translate} %{byteback} %{jar} %s %t
 */
package byteback.test.instance;

public class Supported {

	final Unit support;

	public Supported() {
		this.support = new Unit();
	}

}
/**
 * RUN: %{verify} %t
 * CHECK-IGNORE: Boogie program verifier finished with 3 verified, 0 errors
 */
