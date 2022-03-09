package byteback.dummy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class IntegerPredicates {

    /** 
     * TODO: include annotation definition in SootContext from {@link byteback.annotations}
     */

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD })
    @interface Predicate {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.PARAMETER})
    @interface Old {
    }

    @Predicate
    public static boolean equals(@Old int a, int b) {
        return a == b && a % 2 == 0;
    }

    @Predicate
    public static boolean greaterThan(int a, int b) {
        return a > b;
    }

    @Predicate
    public static boolean lessThan(int a, int b) {
        return a < b;
    }

    @Predicate
    public static boolean greaterThanOrEquals(int a, int b) {
        return a >= b;
    }

    @Predicate
    public static boolean lessThanOrEquals(int a, int b) {
        return a <= b;
    }
    
}
