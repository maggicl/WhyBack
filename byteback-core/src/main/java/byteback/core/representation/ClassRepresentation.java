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
