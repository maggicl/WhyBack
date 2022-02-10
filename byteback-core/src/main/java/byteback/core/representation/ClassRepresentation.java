package byteback.core.representation;

import java.util.Collection;
import java.util.stream.Stream;

import byteback.core.identifier.ClassName;

public interface ClassRepresentation<T extends MethodRepresentation, D extends FieldRepresentation> {

    /**
     * @return The qualified name of the class.
     */
    ClassName getName();

    /**
     * @return The methods stream of the class.
     */
    Stream<T> methods();

    /**
     * @return The fields of the class.
     */
    Stream<D> fields();

}
