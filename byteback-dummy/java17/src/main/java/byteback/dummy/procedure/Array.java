package byteback.dummy.procedure;

public class Array {

	public static int sum(int[] as) {

		int c = 0;

		for (int a : as) {
			c += a;
		}

		return c;
	}

	public static void assignsLastElement(int[] as) {
		as[as.length - 1] = 1;
	}

}
