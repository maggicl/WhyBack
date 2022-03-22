package byteback.dummy;

public class ArrayMethods {

	public static int arraySum(int[] as) {

		int c = 0;

		for (int a : as) {
			c += a;
		}

		return c;
	}

}
