package byteback.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotations for the specification of contracts.
 */
public interface Contract {

    /**
     * Declares a predicate method.
     *
     * A predicate method is a completely pure static method containing boolean
     * statements verifying conditions on its inputs. A predicate method may only
     * indirectly call other static predicate methods.
     * <p>
     * There could possibly be exceptions for certain virtual or interface calls
     * needed for the specification of quantifiers.
     * <p>
     * TODO: delineate such exceptions here.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD })
    public static @interface Pure {
    }

    /**
     * Declares that the function is already defined in the preamble.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD })
    public static @interface Defined {
        public String value();
    }

    /**
     * Declares a condition method.
     * <p>
     * Condition methods can be used to represent preconditions, postconditions or
     * invariants. Every condition method must be both, static and pure. The return
     * type of the annotated method must be void, as conditions are enforced by
     * means of assertions. A condition method may only call predicate methods.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD })
    public static @interface Condition {

        /**
         * Declares a parameter containing the current invocation target.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.PARAMETER })
        public static @interface Instance {
        }

        /**
         * Declares a parameter containing the return value of the invocation.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.PARAMETER })
        public static @interface Returns {
        }

    }

}
