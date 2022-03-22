package byteback.core;

public class RegressionParameter<T> {

	public final T actual;

	public final T expected;

	public RegressionParameter(final T actual, final T expected) {
		this.actual = actual;
		this.expected = expected;
	}

}
