package byteback.core.representation;

import java.util.Collection;

import byteback.core.context.ClassName;
import soot.SootClass;

public class SootClassRepresentation implements ClassRepresentation {

    private final SootClass sootClass;

    private final ClassName name;

    public SootClassRepresentation(final SootClass sootClass) {
        this.sootClass = sootClass;
        this.name = new ClassName(sootClass.getName());
    }

    public ClassName getName() {
        return name;
    }

    public Collection<MethodRepresentation> getMethods() {
        return null;
    }

    public Collection<FieldRepresentation> getFields() {
        return null;
    }

}
