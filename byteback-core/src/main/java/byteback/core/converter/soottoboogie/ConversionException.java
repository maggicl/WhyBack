package byteback.core.converter.soottoboogie;

/**
 * Base exception that may concern the conversion process between Jimple and
 * Boogie.
 *
 * @author paganma
 */
public class ConversionException extends RuntimeException {

	/**
	 * Constructs a new {@link ConversionException}
	 *
	 * @param message
	 *            The message of the exception.
	 */
	public ConversionException(final String message) {
		super(message);
	}

	/**
	 * Constructs a new {@link ConversionException}
	 *
	 * @param message
	 *            The cause of the exception.
	 */
	public ConversionException(final Exception exception) {
		super(exception);
	}

}
