package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import byteback.frontend.boogie.ast.Program;

public class BoogiePreambleIntegrationTest {

    @Test
    public void InitializeProgram_GivenPreamble_DoesNotThrowExceptions() {
        BoogiePrelude.initializeProgram();
    }

    @Test
    public void LoadProgram_CalledTwice_ReturnsTheSameInstance() {
        final Program a = BoogiePrelude.loadProgram();
        final Program b = BoogiePrelude.loadProgram();
        assertEquals(b, a);
    }

    @Test
    public void GetReferenceType_GivenPreamble_DoesNotThrowException() {
        BoogiePrelude.getReferenceType();
    }

    @Test
    public void GetHeapType_GivenPreamble_DoesNotThrowException() {
        BoogiePrelude.getHeapType();
    }

}
