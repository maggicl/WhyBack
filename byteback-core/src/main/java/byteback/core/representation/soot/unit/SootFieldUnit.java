package byteback.core.representation.soot.unit;

import soot.SootField;

public class SootFieldUnit {

    private final SootField sootField;

    public SootFieldUnit(final SootField sootField) {
        this.sootField = sootField;
    }

    public String getName() {
        return sootField.getName();
    }

    public SootClassUnit getClassUnit() {
        return new SootClassUnit(sootField.getDeclaringClass());
    }

}
