package byteback.core;

public class RegressionParameter<T> {

	public final T expected;

	public final T actual;

	public RegressionParameter(final T expected, final T actual) {
		this.expected = expected;
		this.actual = actual;
	}

}
