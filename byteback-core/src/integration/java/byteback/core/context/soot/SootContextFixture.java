package byteback.core.context.soot;

import org.junit.After;

public class SootContextFixture {

    private static final SootContext context = SootContext.instance();

    public static void resetContext() {
        context.reset();
    }

    public static SootContext getContext() {
        return context;
    }

    @After
    public void after() {
        resetContext();
    }

}
