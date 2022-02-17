package byteback.core.type.soot;

import byteback.core.type.Type;

public class SootType implements Type<SootTypeVisitor> {

    private final soot.Type sootType;

    public SootType(final soot.Type sootType) {
        this.sootType = sootType;
    }

    protected int getNumber() {
        return sootType.getNumber();
    }

    @Override
    public void apply(final SootTypeVisitor visitor) {
        sootType.apply(visitor);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof SootType && getNumber() == ((SootType)object).getNumber();
    }

}
