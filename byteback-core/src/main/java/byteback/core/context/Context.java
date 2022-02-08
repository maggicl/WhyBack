package byteback.core.context;

/**
 * A context used to keep track and provide the classes to be analyzed.
 */
public interface Context {

    /**
     * Loads a new class based on the canonical name.
     *
     * @param canonicalName The canonical name of the class.
     */
    public void loadClass(final String canonicalName);

    /**
     * Computes the total number of classes.
     *
     * @return Total number of classes loaded in the context.
     */
    public int getClassesCount();

}
