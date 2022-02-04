package byteback.annotations;

/**
 * Utilities to aid the formulation of complex boolean expressions.
 * 
 * Note that being defined as static functions, none of these operations are
 * short-circuiting. For this reason, using them outside of ByteBack might not
 * be ideal.
 */
public interface Operator {

    /**
     * Boolean implication.
     * 
     * @param a the antecedent of the implication.
     * @param b the consequent of the implication.
     * @return `true` if a implies b.
     */
    public static boolean implies(final boolean a, final boolean b) {
        return !a || b;
    }

    /**
     * Boolean equivalence.
     * 
     * @param a first statement.
     * @param b second statement.
     * @return `true` if a iff b.
     */
    public static boolean iff(final boolean a, final boolean b) {
        return a == b;
    }

}
