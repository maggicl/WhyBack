package byteback.core.converter.soottoboogie;

public class ConversionException extends RuntimeException {

	public ConversionException(final String message) {
		super(message);
	}

	public ConversionException(final Exception exception) {
		super(exception);
	}

}
