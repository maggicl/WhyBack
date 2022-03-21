package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import byteback.frontend.boogie.ast.*;
import org.junit.Test;

public class BoogiePreludeIntegrationTest {

    @Test
    public void InitializeProgram_GivenPrelude_DoesNotThrowExceptions() {
        BoogiePrelude.initializeProgram();
    }

    @Test
    public void LoadProgram_CalledTwice_ReturnsTheSameInstance() {
        final Program a = BoogiePrelude.loadProgram();
        final Program b = BoogiePrelude.loadProgram();
        assertEquals(b, a);
    }

    @Test
    public void GetReferenceType_GivenPrelude_DoesNotThrowException() {
        BoogiePrelude.getReferenceType();
    }

    @Test
    public void GetHeapType_GivenPrelude_DoesNotThrowException() {
        BoogiePrelude.getHeapType();
    }

    @Test
    public void GetHeapVariable_GivenPrelude_DoesNotThrowException() {
        BoogiePrelude.getHeapVariable();
    }

    @Test
    public void GetHeapAccessExpression_GivenPrelude_DoesNotThrowException() {
        final Expression base = new ValueReference(new Accessor("reference"));
        final Expression field = new ValueReference(new Accessor("Reference.field"));
        BoogiePrelude.getHeapAccessExpression(base, field);
    }

}
