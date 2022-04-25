package byteback.dummy.complete;

import byteback.annotations.Contract.Ensure;
import byteback.annotations.Binding;
import static byteback.annotations.Quantifier.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Contract.*;

public class Sum {

	@Pure
	public static boolean positive_arguments_imply_positive_sum(int[] as, int ret) {
		return implies(positive_arguments(as), gte(ret, 0));
	} 

	@Pure
	public static boolean positive_arguments(int[] as) {
		int i = Binding.INTEGER();

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
