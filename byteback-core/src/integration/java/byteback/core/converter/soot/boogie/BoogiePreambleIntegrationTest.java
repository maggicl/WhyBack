package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import byteback.frontend.boogie.ast.Program;

public class BoogiePreambleIntegrationTest {

    @Test
    public void InitializeProgram_GivenPreamble_DoesNotThrowExceptions() {
        BoogiePreamble.initializeProgram();
    }

    @Test
    public void LoadProgram_CalledTwice_ReturnsTheSameInstance() {
        final Program a = BoogiePreamble.loadProgram();
        final Program b = BoogiePreamble.loadProgram();
        assertEquals(b, a);
    }

    @Test
    public void GetReferenceType_GivenPreamble_DoesNotThrowException() {
        BoogiePreamble.getReferenceType();
    }

    @Test
    public void GetHeapType_GivenPreamble_DoesNotThrowException() {
        BoogiePreamble.getHeapType();
    }

}
