package byteback.annotations;

import byteback.annotations.Contract.Attach;
import byteback.annotations.Contract.Return;

@Attach(IndexOutOfBoundsException.class)
public abstract class IndexOutOfBoundsExceptionSpec {

	@Return
	public IndexOutOfBoundsExceptionSpec() {}

	@Return
	public IndexOutOfBoundsExceptionSpec(String message) {}

}
