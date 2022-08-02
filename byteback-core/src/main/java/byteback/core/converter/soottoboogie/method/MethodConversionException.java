package byteback.core.converter.soottoboogie.method;

import byteback.core.converter.soottoboogie.ConversionException;
import soot.SootMethod;

public class MethodConversionException extends ConversionException {

	private final SootMethod method;

	public MethodConversionException(final SootMethod method, final String message) {
		super(message);
		this.method = method;
	}

	public SootMethod getSootMethod() {
		return method;
	}

}
