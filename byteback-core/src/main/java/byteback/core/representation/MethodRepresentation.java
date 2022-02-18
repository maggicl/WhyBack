package byteback.core.representation;

import byteback.core.identifier.MethodName;
import byteback.core.type.Type;

public interface MethodRepresentation<T extends Type<?>> {

    /**
     * Getter for the name of the method.
     *
     * @return The name of the method.
     */
    MethodName getName();

    /**
     * Getter for the return type of the method.
     *
     * @return The return type of the method.
     */
    T getReturnType();

}