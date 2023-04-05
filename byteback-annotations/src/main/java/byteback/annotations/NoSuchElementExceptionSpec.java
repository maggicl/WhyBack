package byteback.annotations;

import java.util.NoSuchElementException;

import byteback.annotations.Contract.Attach;
import byteback.annotations.Contract.Return;

@Attach(NoSuchElementException.class)
public abstract class NoSuchElementExceptionSpec {

	@Return
	public NoSuchElementExceptionSpec() {}

	@Return
	public NoSuchElementExceptionSpec(String message) {}

}
