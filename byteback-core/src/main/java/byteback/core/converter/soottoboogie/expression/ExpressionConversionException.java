package byteback.core.converter.soottoboogie.expression;

import byteback.core.converter.soottoboogie.ConversionException;
import soot.Value;

public class ExpressionConversionException extends ConversionException {

	private final Value value;

	public ExpressionConversionException(final Value value, final Exception exception) {
		super(exception);
		this.value = value;
	}

	public ExpressionConversionException(final Value value, final String message) {
		super(message);
		this.value = value;
	}

	public Value getValue() {
		return value;
	}

}
