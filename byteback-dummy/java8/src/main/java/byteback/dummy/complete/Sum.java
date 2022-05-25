// - linear search (integers, references, generics)
// - max
// - Instance sum

package byteback.dummy.complete;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Quantifier.*;

import byteback.annotations.Binding;
import byteback.annotations.Contract.Ensure;

public class Sum {

	@Pure
	public static boolean positive_arguments_imply_positive_sum(int[] as, int ret) {
		return implies(positive_arguments(as), gte(ret, 0));
	}

	@Pure
	public static boolean positive_arguments(int[] as) {
		int i = Binding.integer();

		return forall(i, implies(lt(i, as.length), gt(as[i], 0)));
	}

	@Ensure("positive_arguments_imply_positive_sum")
	public static int sum(int[] as) {
		int sum = 0;

		for (int i = 0; i < as.length; ++i) {
			invariant(lte(i, as.length));
			invariant(gte(i, 0));
			invariant(implies(positive_arguments(as), gte(sum, 0)));
			sum += as[i];
		}

		return sum;
	}

}
