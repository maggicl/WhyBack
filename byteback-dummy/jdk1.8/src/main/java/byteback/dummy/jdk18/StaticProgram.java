package byteback.dummy.jdk18;

import static byteback.annotations.Operator.*;

import byteback.annotations.Contract.Predicate;

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

}
