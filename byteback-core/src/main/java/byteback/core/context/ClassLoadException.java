package byteback.core.context;

import byteback.core.Name;

/**
 * Represents a failure in loading a class into the {@link Context}.
 */
public class ClassLoadException extends ContextException {

    /**
     * Constructs a {@link ClassLoadException}.
     *
     * @param context   The context that could not load the class.
     * @param className The name of the class that could not be loaded.
     */
    public ClassLoadException(Context<?> context, Name className) {
        super(context, "Could not load class " + className.toString());
    }

}
