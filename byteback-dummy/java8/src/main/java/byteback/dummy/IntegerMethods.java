package byteback.dummy;

import byteback.annotations.Contract.Predicate;

import static byteback.annotations.Operator.eq;
import static byteback.annotations.Operator.not;

public class IntegerMethods {

    @Predicate
    public static boolean even(int a) {
        return eq(a % 2, 0);
    }

    public static boolean odd(int a) {
        return not(even(a));
    }

}
