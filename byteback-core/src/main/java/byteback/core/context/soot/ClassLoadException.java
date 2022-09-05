package byteback.core.context.soot;

/**
 * Represents a failure in loading a class into the {@link SootContext}.
 */
public class ClassLoadException extends ContextException {

	/**
	 * Constructs a new {@link ClassLoadException}.
	 *
	 * @param className
	 *            The name of the class that could not be loaded.
	 */
	public ClassLoadException(String className) {
		super("Could not load class " + className);
	}

}
