package byteback.core.type.soot;

import byteback.core.type.Type;

public class SootType implements Type<SootTypeVisitor> {

    private final soot.Type sootType;

    public SootType(final soot.Type sootType) {
        this.sootType = sootType;
    }

    @Override
    public void apply(final SootTypeVisitor visitor) {
        sootType.apply(visitor);
    }

}
