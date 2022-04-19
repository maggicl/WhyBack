package byteback.core.converter.soottoboogie.statement;

import byteback.core.converter.soottoboogie.ConversionException;
import soot.Unit;

public class StatementConversionException extends ConversionException {

	private final Unit unit;

	public StatementConversionException(final Unit unit, final String message) {
		super(message);
		this.unit = unit;
	}

	public StatementConversionException(final Unit unit, final Exception exception) {
		super(exception);
		this.unit = unit;
	}

	public StatementConversionException(final Unit unit) {
		this(unit, "Failed to convert statement " + unit);
	}

	public Unit getUnit() {
		return unit;
	}

}
