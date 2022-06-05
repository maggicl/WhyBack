package byteback.core.representation;

public interface Visitable<T extends Visitor<?, ?>> {

	void apply(T visitor);

}
