package byteback.core.converter.soottoboogie;

/**
 * Base exception that may concern the conversion process between Jimple and
 * Boogie.
 *
 * @author paganma
 */
public class ConversionException extends RuntimeException {

	public ConversionException(final String message) {
		super(message);
	}

	public ConversionException(final Exception exception) {
		super(exception);
	}

}
