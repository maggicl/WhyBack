package byteback.core;

public interface Visitable<T extends Visitor> {

    void apply(T visitor);

}
