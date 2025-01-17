/**
 * RUN: %{translate} %{byteback} %{jar} %s %t
 */
package byteback.test.operation;

import byteback.annotations.Contract.Pure;

public class StaticCall {

	@Pure
	public static int getConstant() {
		return 1;
	}

	@Pure
	public static int increment(int a) {
		return a + 1;
	}

	public static int proceduralIncrement(int a) {
		return a + 1;
	}

	@Pure
	public static int main() {
		return increment(getConstant());
	}

	public static int callsPure() {
		return increment(getConstant());
	}

	public static int callsProcedural() {
		return increment(proceduralIncrement(getConstant()));
	}

}
/**
 * RUN: %{verify} %t
 * CHECK-IGNORE: Boogie program verifier finished with 4 verified, 0 errors
 */
