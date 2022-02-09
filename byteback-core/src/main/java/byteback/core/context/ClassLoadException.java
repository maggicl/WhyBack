package byteback.core.context;

/**
 * Represents a failure in loading a class into the {@link Context}.
 */
public class ClassLoadException extends ContextException {

    /**
     * Constructs a basic context exception.
     *
     * @param context The context that could not load the class.
     * @param
     */
    public ClassLoadException(Context context, QualifiedName className) {
        super(context, "Could not load class " + className.toString());
    }

}
