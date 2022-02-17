package byteback.core.representation;

import java.util.stream.Stream;

import byteback.core.identifier.ClassName;
import byteback.core.type.Type;

public interface ClassRepresentation<T extends Type<?>, F extends FieldRepresentation<T>, M extends MethodRepresentation<T>> {

    /**
     * Getter for the qualified name of the class.
     *
     * @return The qualified name of the class.
     */
    ClassName getName();

    /**
     * Getter for the type corresponding to the class.
     *
     * @return The type corresponding to the class.
     */
    T getType();

    /**
     * Getter for the stream of method representations.
     *
     * @return The methods stream of the class.
     */
    Stream<M> methods();

    /**
     * Getter for the stream of field representations.
     *
     * @return The fields of the class.
     */
    Stream<F> fields();

    /**
     * Checks that the class referred by this representation is final.
     *
     * @return {@code true} if the method is final.
     */
    boolean isFinal();

    /**
     * Checks that the class referred by this representation is final.
     *
     * @return {@code true} if the method is final.
     */
    boolean isStatic();

    /**
     * Checks that the class referred by this representation is final.
     *
     * @return {@code true} if the method is final.
     */
    boolean isAbstract();

}
