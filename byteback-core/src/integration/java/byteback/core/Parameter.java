package byteback.core;

public class Parameter<T> {

	public final T expected;

	public final T actual;

	public Parameter(final T expected, final T actual) {
		this.expected = expected;
		this.actual = actual;
	}

}
