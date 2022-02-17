package byteback.core.representation.soot;

import byteback.core.representation.FieldRepresentation;
import byteback.core.Name;
import byteback.core.type.soot.SootType;
import soot.SootField;

public class SootFieldRepresentation implements FieldRepresentation<SootType> {

    private final SootField sootField;

    private final Name name;

    public SootFieldRepresentation(final SootField sootField) {
        this.sootField = sootField;
        this.name = Name.get(sootField.getName());
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public SootType getType() {
        return new SootType(sootField.getType());
    }

}
