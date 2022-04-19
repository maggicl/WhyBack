package byteback.core.converter.soottoboogie.method.function;

import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.representation.soot.unit.SootMethod;

public class FunctionConversionException extends ConversionException {

	final SootMethod method;

	public FunctionConversionException(final SootMethod method, final String message) {
		super(message);
		this.method = method;
	}

	public FunctionConversionException(final SootMethod method, final Exception exception) {
		super(exception);
		this.method = method;
	}

	@Override
	public String getMessage() {

		return "Exception while converting pure method " + method.getIdentifier() + ":\n" +
				super.getMessage();
	}

}
