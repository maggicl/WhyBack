package byteback.dummy.complete;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;

public class LinearSearch {

	@Condition
	public static boolean bounded_indices(int a[], int n, int left, int right) {
		return lte(0, left) & lte(left, right) & lte(right, a.length);
	}

	@Condition
	public static boolean result_is_index(int a[], int n, int left, int right, int returns) {
		return implies(lte(0, returns), eq(a[returns], n));
	}

	@Require("bounded_indices")
	@Ensure("result_is_index")
	public static int search(int a[], int n, int left, int right) {

		for (int i = left; i < right; ++i) {
			invariant(lte(left, i) & lte(i, right));

			if (a[i] == n) {
				return i;
			}
		}

		return -1;
	}
}
