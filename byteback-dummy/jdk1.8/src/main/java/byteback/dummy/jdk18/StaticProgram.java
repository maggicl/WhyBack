package byteback.dummy.jdk18;

import static byteback.annotations.Operator.*;

import byteback.annotations.Contract.Condition;
import byteback.annotations.Contract.Predicate;
import byteback.annotations.Contract.Condition.Returns;

public class StaticProgram {

    @Predicate
    static boolean equals(int a, int b) {
        return a == b;
    }

    @Predicate
    static boolean positive(int a) {
        return a >= 0;
    }

    @Predicate
    static boolean sum_of_positive_is_positive(int a, int b) {
        return implies(positive(a) && positive(b), positive(a + b));
    }

    @Condition
    static void f_precondition(int a, int b, int c) {
        assert equals(a, b);
        assert equals(b, c);
    }

    @Condition
    static void f_postcondition(int a, int b, int c, @Returns int returns) {
        assert returns == a * 3;
    }

    static int f(int a, int b, int c) {
        return a + b + c;
    }

}
