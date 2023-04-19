package byteback.annotations;

import java.util.ConcurrentModificationException;

import byteback.annotations.Contract.Attach;
import byteback.annotations.Contract.Return;

@Attach(ConcurrentModificationException.class)
public class ConcurrentModificationExceptionSpec {

	@Return
	public ConcurrentModificationExceptionSpec() {}

	@Return
	public ConcurrentModificationExceptionSpec(String message) {}

}
