package byteback.core.representation;

import byteback.core.type.Name;
import byteback.core.type.Type;

public interface MethodRepresentation<T extends Type<?>> {

    /**
     * Getter for the name of the method.
     *
     * @return The name of the method.
     */
    Name getName();

    /**
     * Getter for the return type of the method.
     *
     * @return The return type of the method.
     */
    T getReturnType();

    /**
     * Checks that the method referred by this representation is private.
     *
     * @return {@code true} if the method is private.
     */
    boolean isPrivate();

    /**
     * Checks that the method referred by this representation is protected.
     *
     * @return {@code true} if the method is protected.
     */
    boolean isProtected();

    /**
     * Checks that the method referred by this representation is public.
     *
     * @return {@code true} if the method is public.
     */
    boolean isPublic();

    /**
     * Checks that the method referred by this representation is static.
     *
     * @return {@code true} if the method is static.
     */
    boolean isStatic();

    /**
     * Checks that the method referred by this representation is abstract.
     *
     * @return {@code true} if the method is abstract.
     */
    boolean isAbstract();

}
