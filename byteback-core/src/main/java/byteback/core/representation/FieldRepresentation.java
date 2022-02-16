package byteback.core.representation;

import byteback.core.Visitor;
import byteback.core.type.Name;
import byteback.core.type.Type;

public interface FieldRepresentation<T extends Type<?>> {

    /**
     * Getter for the name of the field.
     *
     * @return The name of the field.
     */
    Name getName();

    /**
     * Getter for the type of the field.
     *
     * @return The type of the field.
     */
    T getType();

}
