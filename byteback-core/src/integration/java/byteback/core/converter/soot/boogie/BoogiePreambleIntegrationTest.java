package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import byteback.frontend.boogie.ast.Program;

public class BoogiePreambleIntegrationTest {

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

}
