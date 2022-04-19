package byteback.core.converter.soottoboogie.expression;

import soot.Value;

public class ExpressionConversionException extends RuntimeException {

	public ExpressionConversionException(final Exception exception) {
		super(exception);
	}

	public ExpressionConversionException(final String message) {
		super(message);
	}

	public ExpressionConversionException(final Value value) {
		super("Failed to convert expression " + value);
	}

}
