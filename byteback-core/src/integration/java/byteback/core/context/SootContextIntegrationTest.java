package byteback.core.context;

import static org.junit.Assert.assertEquals;

public class SootContextIntegrationTest {

    public void Instance_CalledTwice_ReturnsSameContext() {
        SootContext a = SootContext.instance();
        SootContext b = SootContext.instance();
        assertEquals(a, b);
    }

}
