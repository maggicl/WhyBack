package byteback.core.context;

/**
 * Represents any exception that may concern the services provided by a
 * {@link Context} class.
 */
public abstract class ContextException extends Exception {

    /**
     * Constructs a basic context exception.
     *
     * @param context The context in which the exception occurred.
     * @param message The message of the exception.
     */
    public ContextException(Context<?> context, String message) {
        super(context.toString() + " : " + message);
    }

}
