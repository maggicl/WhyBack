package byteback.analysis;

public interface Visitor<T, R> {

	void caseDefault(T t);
	default R result() {
		return null;
	}

}
