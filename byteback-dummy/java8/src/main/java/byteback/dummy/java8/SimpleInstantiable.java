package byteback.dummy.jdk18;

import byteback.annotations.Contract.Condition;
import byteback.annotations.Contract.Predicate;
import byteback.annotations.Contract.Condition.Current;
import byteback.annotations.Contract.Condition.Old;

public class SimpleInstantiable {

    public int a;

    public int b;

    @Predicate
    static boolean equals(int a, int b) {
        return a == b;
    }

    @Condition
    public void SimpleInstantiable_invariant(@Old SimpleInstantiable old, @Current SimpleInstantiable current) {
        assert equals(old.a, current.b);
    }

    public void setA(int a) {
        this.a = a;
        this.b = a;
    }

    public void setB(int b) {
        this.a = b;
        this.b = b;
    }

    public int getA() {
        return this.a;
    }

    public int getB() {
        return this.b;
    }

}
