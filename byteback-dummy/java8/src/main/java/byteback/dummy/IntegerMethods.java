package byteback.dummy;

import static byteback.annotations.Operator.and;
import static byteback.annotations.Operator.eq;
import static byteback.annotations.Operator.not;

public class IntegerMethods {

    public static boolean even(int a) {
        boolean b = false;
        return and(eq(a % 2, 0), b);
    }

    public static boolean odd(int a) {
        return not(even(a));
    }

}
