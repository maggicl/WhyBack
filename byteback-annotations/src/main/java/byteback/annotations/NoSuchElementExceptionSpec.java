package byteback.annotations;

import byteback.annotations.Contract.Attach;
import byteback.annotations.Contract.Return;
import java.util.NoSuchElementException;

@Attach(NoSuchElementException.class)
public abstract class NoSuchElementExceptionSpec {

	@Return
	public NoSuchElementExceptionSpec() {
	}

	@Return
	public NoSuchElementExceptionSpec(String message) {
	}

}
