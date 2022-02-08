package byteback.core.context;

/**
 * A context used to keep track and provide the classes to be analyzed.
 */
public interface Context {

    /**
     * Loads a new class based on the canonical name.
     *
     * @param qualifiedName The qualified name of the class.
     */
    public void loadClass(final QualifiedName qualifiedName);

    /**
     * Computes the total number of classes.
     *
     * @return Total number of classes loaded in the context.
     */
    public int getClassesCount();

}
