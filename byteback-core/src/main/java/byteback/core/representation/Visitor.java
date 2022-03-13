package byteback.core.representation;

public interface Visitor<T, R> {

    void caseDefault(T argument);

    R result();

    @SuppressWarnings("unchecked")
    default <S extends Visitor<T, ?>> R visit(final Visitable<S> visitable) {
        visitable.apply((S) this);

        return result();
    }

}