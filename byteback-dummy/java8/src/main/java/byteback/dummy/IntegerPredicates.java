package byteback.dummy;

public class IntegerPredicates {

    /** 
     * TODO: include annotation definition in SootContext from {@link byteback.annotations}
     */
    @interface Predicate {
    }

    @Predicate
    public static boolean equals(int a, int b) {
        return a == b;
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
