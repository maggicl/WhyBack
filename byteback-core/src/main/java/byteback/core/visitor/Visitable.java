package byteback.core.visitor;

public interface Visitable<T extends Visitor> {

    void apply(T visitor);

}
