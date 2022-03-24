package byteback.dummy.procedure;

public class Array {

	public static int arraySum(int[] as) {

		int c = 0;

		for (int a : as) {
			c += a;
		}

		return c;
	}

}
