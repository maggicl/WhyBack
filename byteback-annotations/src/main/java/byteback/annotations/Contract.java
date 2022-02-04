package byteback.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface Contract {

    /**
     * Declares a predicate method.
     *
     * A predicate method is a completely pure static method
     * containing boolean statements verifying conditions on its
     * inputs. A predicate method may only indirectly call other
     * static predicate methods.
     *
     * There could possibly be exceptions for certain virtual or
     * interface calls needed for the specification of quantifiers.
     * TODO: delineate such exceptions here.
     */
    @Retention(RetentionPolicy.CLASS)
    @Target({ ElementType.METHOD })
    public static @interface Predicate {}

}
