package byteback.core.representation;

import java.util.Collection;

import byteback.core.context.ClassName;

public interface ClassRepresentation {

    /**
     * @return The name of the class.
     */
    ClassName getName();

    /**
     * @return The methods of the class.
     */
    Collection<MethodRepresentation> getMethods();

    /**
     * @return The fields of the class.
     */
    Collection<FieldRepresentation> getFields();

}
