package byteback.core.context;

import java.util.stream.Stream;

import byteback.core.identifier.QualifiedName;
import byteback.core.representation.ClassRepresentation;

/**
 * Represents a context keeping track of the classes to be analyzed.
 *
 * @param <T> The type of class representation provided by the context.
 */
public interface Context<T extends ClassRepresentation<?, ?>> {

    /**
     * Loads a new class based on the canonical name.
     *
     * @param className The qualified name of the class.
     * @return The loaded intermediate representation of the class.
     * @throws ClassLoadException If the class could not be loaded into the context.
     */
    T loadClass(QualifiedName className) throws ClassLoadException;

    /**
     * Loads a new class based on the canonical name along with its supporting
     * classes.
     * 
     * @param className The qualified name of the root class.
     * @return The loaded intermediate representation of the root class.
     * @throws ClassLoadException If the class, or one of its supporting classes
     *                            could not be loaded into the context.
     */
    T loadClassAndSupport(QualifiedName className) throws ClassLoadException;

    /**
     * Computes the total number of classes.
     *
     * @return Total number of classes loaded in the context.
     */
    int getClassesCount();

    /**
     * Streams all the class representations loaded into the context.
     *
     * @return The stream of class representations supported by the context.
     */
    Stream<T> classes();

    /**
     * Function returning the name of the context.
     *
     * @return The name string of the context.
     */
    String getName();

}
