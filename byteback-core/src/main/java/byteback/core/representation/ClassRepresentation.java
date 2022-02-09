package byteback.core.representation;

import java.util.Collection;

import byteback.core.context.QualifiedName;

public interface ClassRepresentation {

    /**
     * @return The name of the class.
     */
    public QualifiedName getName();

    /**
     * @return The methods of the class.
     */
    public Collection<MethodRepresentation> getMethods();

    /**
     * @return The fields of the class.
     */
    public Collection<FieldRepresentation> getFields();

}
