package byteback.core.context.soot;

/**
 * Represents an exception that may concern the services provided by a
 * {@link SootContext} class.
 */
public abstract class ContextException extends RuntimeException {

	/**
	 * Constructs a basic context exception.
	 *
	 * @param message
	 *            The message of the exception.
	 */
	public ContextException(String message) {
		super(message);
	}

}
