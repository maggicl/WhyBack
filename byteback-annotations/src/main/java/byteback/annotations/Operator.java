package byteback.annotations;

/**
 * Utilities to aid the formulation of complex boolean expressions.
 * <p>
 * Note that being defined as static functions, none of these operations are
 * short-circuiting. For this reason, using them outside of ByteBack might not
 * be ideal.
 */
public interface Operator {

    /**
     * Boolean implication.
     * 
     * @param a Antecedent of the implication.
     * @param b Consequent of the implication.
     * @return {@code true} if {@code a -> b}.
     */
    public static boolean implies(final boolean a, final boolean b) {
        return !a || b;
    }

    /**
     * Boolean equivalence.
     * 
     * @param a First statement.
     * @param b Second statement.
     * @return {@code true} if {@code a <-> b}.
     */
    public static boolean iff(final boolean a, final boolean b) {
        return a == b;
    }

}
