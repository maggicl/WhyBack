package byteback.core.context;

import java.util.stream.Stream;

import byteback.core.identifier.ClassName;
import byteback.core.representation.ClassRepresentation;

/**
 * A context used to keep track of the classes to be analyzed.
 *
 * @param <T> The type of class representation provided by the context.
 */
public interface Context<T extends ClassRepresentation> {

    /**
     * Loads a new class based on the canonical name.
     *
     * @param className The qualified name of the class.
     * @throws ClassLoadException If the class could not be loaded into the context.
     */
    void loadClass(ClassName className) throws ClassLoadException;

    /**
     * Loads a new class based on the canonical name along with its supporting
     * classes.
     *
     * @see #loadClass(ClassName)
     */
    void loadClassAndSupport(ClassName className) throws ClassLoadException;

    /**
     * Computes the total number of classes.
     *
     * @return Total number of classes loaded in the context.
     */
    int getClassesCount();

    /**
     * Streams all of the loaded classes.
     *
     * @return The stream of class representations supported by the context.
     */
    Stream<T> stream();

}
