package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import byteback.frontend.boogie.ast.Program;

public class BoogiePreambleIntegrationTest {

    @Test
    public void Initialize_GivenPreamble_DoesNotThrowExceptions() {
        BoogiePreamble.initialize();
    }

    @Test
    public void Load_CalledTwice_ReturnsTheSameInstance() {
        final Program a = BoogiePreamble.load();
        final Program b = BoogiePreamble.load();
        assertEquals(b, a);
    }

}
