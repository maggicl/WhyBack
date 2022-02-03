package byteback.annotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static byteback.annotations.Operator.*;

import org.junit.Test;

public class OperatorTest {

    @Test
    public void Implies_AnyTrue_ReturnsTrue() {
        assertTrue(implies(true, true));
        assertTrue(implies(false, true));
    }

    @Test
    public void Implies_FalseFalse_ReturnsTrue() {
        assertTrue(implies(false, false));
    }

    @Test
    public void Implies_TrueFalse_ReturnsFalse() {
        assertFalse(implies(true, false));
    }
    
}
