package byteback.annotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import static byteback.annotations.Quantifier.*;

import org.junit.Test;

public class QuantifierTest {

    final private Iterable<Object> EMPTY = Collections.emptyList();

    final private Iterable<Integer> INTEGER_LIST = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

    @Test
    public void Exists_EmptyArgument_ReturnsFalse() {
        assertFalse(exists(EMPTY, (x) -> true));
    }

    @Test
    public void Exists_IntListSatisfyingPredicate_ReturnsTrue() {
        assertTrue(exists(INTEGER_LIST, (x) -> x < 1));
        assertTrue(exists(INTEGER_LIST, (x) -> x > 0 && x < 9));
        assertTrue(exists(INTEGER_LIST, (x) -> x > 8));
    }

    @Test
    public void Exists_IntListNotSatisfyingPredicate_ReturnsTrue() {
        assertFalse(exists(INTEGER_LIST, (x) -> x < 0));
        assertFalse(exists(INTEGER_LIST, (x) -> x < 0 || x > 9));
        assertFalse(exists(INTEGER_LIST, (x) -> x > 9));
    }
    
    @Test
    public void Forall_EmptyArgument_ReturnsTrue() {
        assertFalse(exists(EMPTY, (x) -> false));
    }

    @Test
    public void Forall_IntListSatisfyyingPredicate_ReturnsTrue() {
        assertTrue(forAll(INTEGER_LIST, (x) -> x >= 0 && x <= 9));
    }

    @Test
    public void Forall_IntListNotSatisfyyingPredicate_ReturnsTrue() {
        assertFalse(forAll(INTEGER_LIST, (x) -> x != 4));
        assertFalse(forAll(INTEGER_LIST, (x) -> x != 5));
    }

}
