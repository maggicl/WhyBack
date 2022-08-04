package byteback.dummy.procedure;

import java.util.function.Function;

public class Lambda {

	public static Function<Integer, Integer> returnsLambda() {
		return (x) -> x + 1;
	}

	public static int usesLambda() {
		int a = 0;

		return returnsLambda().apply(a);
	}

}
