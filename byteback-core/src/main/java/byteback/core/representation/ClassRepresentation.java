package byteback.core.representation;

import java.util.stream.Stream;

import byteback.core.identifier.Name;
import byteback.core.type.Type;

public interface ClassRepresentation<T extends Type<?>, F extends FieldRepresentation<T>, M extends MethodRepresentation<T>> {

    /**
     * Getter for the qualified name of the class.
     *
     * @return The qualified name of the class.
     */
    Name getName();

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

}
