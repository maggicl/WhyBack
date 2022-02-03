package byteback.annotations;

/**
 * Utilities to formulate complex boolean expressions.
 * 
 * Note that being defined as static functions, none of these
 * operations are short-circuiting. For this reason, using them
 * outside of ByteBack might not be ideal.
 */
public abstract class Operator {

    /**
     * Boolean implication.
     * @param a the antecedent of the implication.
     * @param b the consequent of the implication.
     * @return `true` if a implies b.
     */
    public static boolean implies(boolean a, boolean b) {
        return !a || b;
    }

}
