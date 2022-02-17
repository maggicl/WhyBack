package byteback.core.representation.soot;

import byteback.core.identifier.MemberName;
import byteback.core.representation.FieldRepresentation;
import byteback.core.type.soot.SootType;
import soot.SootField;

public class SootFieldRepresentation implements FieldRepresentation<SootType> {

    private final SootField sootField;

    private final MemberName name;

    public SootFieldRepresentation(final SootField sootField) {
        this.sootField = sootField;
        this.name = new MemberName(sootField.getName());
    }

    @Override
    public MemberName getName() {
        return name;
    }

    @Override
    public SootType getType() {
        return new SootType(sootField.getType());
    }

}
