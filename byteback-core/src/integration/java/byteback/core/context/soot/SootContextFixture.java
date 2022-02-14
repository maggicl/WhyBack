package byteback.core.context.soot;

import org.junit.After;
import org.junit.BeforeClass;

public class SootContextFixture {

    private static final SootContext context = SootContext.instance();

    @BeforeClass
    public static void initialize() {
        context.reset();
    }

    @After
    public void resetContext() {
        context.reset();
    }

    public SootContext getContext() {
        return context;
    }

}