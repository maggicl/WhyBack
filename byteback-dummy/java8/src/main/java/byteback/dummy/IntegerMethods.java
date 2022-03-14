package byteback.dummy;

import static byteback.annotations.Operator.eq;
import static byteback.annotations.Operator.not;

import byteback.annotations.Contract.Pure;

public class IntegerMethods {

    @Pure
    public static boolean even(int a) {
        return eq(a % 2, 0);
    }

    @Pure
    public static boolean odd(int a) {
        return not(even(a));
    }

}
