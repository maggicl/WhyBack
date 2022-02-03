package byteback.annotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static byteback.annotations.Quantifier.*;

import org.junit.Test;

public class QuantifierTest {

    final private Iterable<Object> EMPTY = Collections.emptyList();

    @Test
    public void Exists_EmptyAlwaysTruePredicate_ReturnsFalse() {
        assertFalse(exists(EMPTY, (x) -> true));
    }

    @Test
    public void Exists_IntListSatisfyingPredicate_ReturnsTrue() {
        final List<Integer> d = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertTrue(exists(d, (x) -> x < 1));
        assertTrue(exists(d, (x) -> x > 0 && x < 9));
        assertTrue(exists(d, (x) -> x > 8));
    }

    @Test
    public void Exists_IntListNotSatisfyingPredicate_ReturnsTrue() {
        final List<Integer> d = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertFalse(exists(d, (x) -> x < 0));
        assertFalse(exists(d, (x) -> x < 0 || x > 9));
        assertFalse(exists(d, (x) -> x > 9));
    }
    
}
