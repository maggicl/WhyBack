package byteback.core.context.soot;

public class SootContextFixture {

	private static final SootContext context = SootContext.instance();

	public static void resetContext() {
		context.reset();
	}

	public static SootContext getContext() {
		return context;
	}

}
