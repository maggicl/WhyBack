package byteback.core.representation;

public interface Visitor<T> {

    void caseDefault(T object);

}
