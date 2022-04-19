package byteback.core.converter.soottoboogie.method.procedure;

import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.representation.soot.unit.SootMethod;

public class ProcedureConversionException extends ConversionException {

	final SootMethod method;

	public ProcedureConversionException(final SootMethod method, final String message) {
		super(message);
		this.method = method;
	}

	public ProcedureConversionException(final SootMethod method, final Exception exception) {
		super(exception);
		this.method = method;
	}

	@Override
	public String getMessage() {

		return "Exception while converting procedure method " + method.getIdentifier() + ":\n" +
				super.getMessage();
	}

}
