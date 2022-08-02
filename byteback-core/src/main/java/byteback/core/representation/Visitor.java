package byteback.core.representation;

public interface Visitor<T, R> {

	default void caseDefault(T argument) {
	}

	default R result() {
		return null;
	}

}
