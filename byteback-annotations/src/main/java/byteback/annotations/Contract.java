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
     *
     * There could possibly be exceptions for certain virtual or interface calls
     * needed for the specification of quantifiers.
     *
     * TODO: delineate such exceptions here.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD })
    public static @interface Predicate {
    }

    /**
     * Declares a condition method.
     *
     * Condition methods can be used to represent preconditions, postconditions or
     * invariants. Every condition method must be both, static and pure. The return
     * type of the annotated method must be void, as conditions are enforced by
     * means of assertions. A condition method may only call predicate methods.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD })
    public static @interface Condition {

        /**
         * Declares a parameter containing the old invocation target.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.PARAMETER })
        public static @interface Old {
        }

        /**
         * Declares a parameter containing the current invocation target.
         */
        @Retention(RetentionPolicy.RUNTIME)
        @Target({ ElementType.PARAMETER })
        public static @interface Current {
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
