package byteback.core.context;

/**
 * A context used to keep track and provide the classes to be analyzed.
 */
public interface Context {

    /**
     * Loads a new class based on the canonical name.
     *
     * @param className The qualified name of the class.
     * @throws ClassLoadException If the class could not be loaded into the context.
     */
    public void loadClass(QualifiedName className) throws ClassLoadException;

    /**
     * Loads a new class based on the canonical name along with its supporting
     * classes.
     *
     * @see #loadClass(QualifiedName)
     */
    public void loadClassAndSupport(QualifiedName className) throws ClassLoadException;

    /**
     * Computes the total number of classes.
     *
     * @return Total number of classes loaded in the context.
     */
    public int getClassesCount();

}
