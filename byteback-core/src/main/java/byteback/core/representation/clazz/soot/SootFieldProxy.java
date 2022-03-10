package byteback.core.representation.clazz.soot;

import soot.SootField;

public class SootFieldProxy {

    private final SootField sootField;

    public SootFieldProxy(final SootField sootField) {
        this.sootField = sootField;
    }

    public String getName() {
        return sootField.getName();
    }

    public SootField getSootField() {
        return sootField;
    }

}
