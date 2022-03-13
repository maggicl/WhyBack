package byteback.dummy;

public class IntegerMethods {

    public static boolean eq(int a, int b) {
        return true;
    }

    public static boolean not(boolean a) {
        return false;
    }

    public static boolean even(int a) {
        return eq(a % 2, 0);
    }

    public static boolean odd(int a) {
        return not(even(a));
    }

}
