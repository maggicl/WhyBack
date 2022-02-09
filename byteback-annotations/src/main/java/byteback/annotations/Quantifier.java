package byteback.annotations;

import java.util.function.Predicate;

/**
 * Utilities to express quantified expressions.
 */
public interface Quantifier {

    /**
     * Existential quantifier.
     * 
     * @param d   The domain of discourse.
     * @param p   The predicate applied to the domain.
     * @param <T> Type of the iterated elements.
     * @return {@code true} if there is a term in {@code d} that satisfies
     *         {@code p}.
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
     * 
     * @param d   The domain of discourse.
     * @param p   The predicate applied to the domain.
     * @param <T> Type of the iterated elements.
     * @return {@code true} if all terms in {@code d} satisfy {@code p}.
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
