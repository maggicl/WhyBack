package byteback.core.representation;

public interface Visitor<T, R> {

  default void caseDefault(T argument) {
  }

	default R result() {
		return null;
	}

	@SuppressWarnings("unchecked")
	default <S extends Visitor<T, ?>> R visit(final Visitable<S> visitable) {
		visitable.apply((S) this);

		return result();
	}

}
