package byteback.core.representation;

import java.util.Collection;

import byteback.core.context.QualifiedName;
import soot.SootClass;

public class SootClassRepresentation implements ClassRepresentation {

    private final SootClass sootClass;

    private final QualifiedName name;

    public SootClassRepresentation(final SootClass sootClass) {
        this.sootClass = sootClass;
        this.name = new QualifiedName(sootClass.getName());
    }

    public QualifiedName getName() {
        return name;
    }

    public Collection<MethodRepresentation> getMethods() {
        return null;
    }

    public Collection<FieldRepresentation> getFields() {
        return null;
    }

}
