/**
 * RUN: %{byteback} -cp %{jar} -c byteback.test.lambda.Basic -o %t.mlw
 */
package byteback.test.lambda;

import java.util.function.Function;

public class Basic {

	public static Function<Integer, Integer> returnsLambda() {
		return (x) -> x + 1;
	}

	public static int usesLambda() {
		int a = 0;

		return returnsLambda().apply(a);
	}

}
