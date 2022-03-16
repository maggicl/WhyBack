package byteback.dummy;

import byteback.annotations.Contract.Pure;

public class BooleanMethods {

    @Pure
    public static boolean or(boolean a, boolean b) {
        return a | b;
    }

    @Pure
    public static boolean and(boolean a, boolean b) {
        return a & b;
    }

}
