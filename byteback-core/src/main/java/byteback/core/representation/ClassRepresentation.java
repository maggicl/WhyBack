package byteback.core.representation;

import java.util.stream.Stream;

import byteback.core.identifier.QualifiedName;

public interface ClassRepresentation<T extends MethodRepresentation, D extends FieldRepresentation> {

    /**
     * Getter for the qualified name of the class.
     *
     * @return The qualified name of the class.
     */
    QualifiedName getQualifiedName();

    /**
     * Getter for the stream of method representations.
     *
     * @return The methods stream of the class.
     */
    Stream<T> methods();

    /**
     * Getter for the stream of field representations.
     *
     * @return The fields of the class.
     */
    Stream<D> fields();

}
