package byteback.core;

public interface Visitor<T> {

    void caseDefault(T object);

}
