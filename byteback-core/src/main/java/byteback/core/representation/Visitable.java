package byteback.core.representation;

public interface Visitable<T extends Visitor<?, ?>> {

    void apply(T visitor);

    public static <R, T extends Visitor<?, R>> R compute(Visitable<Visitor<?, ? extends R>> visitable, T visitor) {
        visitable.apply(visitor);

        return visitor.result();
    }

}
