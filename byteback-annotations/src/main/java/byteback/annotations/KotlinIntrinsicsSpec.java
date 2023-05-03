package byteback.annotations;

import byteback.annotations.Contract.AttachLabel;
import byteback.annotations.Contract.Lemma;
import byteback.annotations.Contract.Raise;
import static byteback.annotations.Operator.*;

@AttachLabel("Lkotlin/jvm/internal/Intrinsics;")
public abstract class KotlinIntrinsicsSpec {

	public static boolean parameter_is_null(Object parameter, String name) {
		return neq(parameter, null);
	}

	@Lemma
	@Raise(exception = IllegalArgumentException.class, when = "parameter_is_null")
	public static void checkNotNullParameter(Object parameter, String name) {}

}
