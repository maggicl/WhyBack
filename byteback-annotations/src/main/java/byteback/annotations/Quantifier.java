package byteback.annotations;

import java.util.function.Predicate;

/**
 * Utilities to express quantified expressions.
 * 
 */
public interface Quantifier {

    /**
     * Existential quantifier.
     * @param d the domain of discourse.
     * @param p the predicate applied to the domain.
     * @param <T> type of the iterated elements.
     * @return `true` if there exists a term in d that satisfies p.
     */
    public static <T> boolean exists(final Iterable<T> d, final Predicate<T> p) {
        for (T x : d) {
            if (p.test(x)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Universal quantifier.
     * @param d the domain of discourse.
     * @param p the predicate applied to the domain.
     * @param <T> type of the iterated elements.
     * @return `true` if all terms in d satisfy p.
     */
    public static <T> boolean forAll(final Iterable<T> d, final Predicate<T> p) {
        for (T x : d) {
            if (!p.test(x)) {
                return false;
            }
        }

        return true;
    }

}
