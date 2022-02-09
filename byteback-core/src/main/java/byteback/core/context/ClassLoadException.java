package byteback.core.context;

import byteback.core.identifier.ClassName;

/**
 * Represents a failure in loading a class into the {@link Context}.
 */
public class ClassLoadException extends ContextException {

    /**
     * Constructs a basic context exception.
     *
     * @param context The context that could not load the class.
     * @param className The name of the class that could not be loaded.
     */
    public ClassLoadException(Context<?> context, ClassName className) {
        super(context, "Could not load class " + className.toString());
    }

}
