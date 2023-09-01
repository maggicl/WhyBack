package byteback.annotations;

import byteback.annotations.Contract.Attach;
import byteback.annotations.Contract.Return;

@Attach(NullPointerException.class)
public abstract class NullPointerExceptionSpec {

	@Return
	public NullPointerExceptionSpec() {
	}

	@Return
	public NullPointerExceptionSpec(String message) {
	}

}
