package byteback.core.representation.soot;

import byteback.core.identifier.Name;
import byteback.core.representation.MethodRepresentation;
import soot.SootMethod;

public class SootMethodIR implements MethodRepresentation {

    private final SootMethod sootMethod;

    private final Name relativeName;

    /**
     * Constructor for the
     *
     * @param sootMethod The wrapped {@code SootMethod} instance.
     */
    public SootMethodIR(final SootMethod sootMethod) {
        this.sootMethod = sootMethod;
        this.relativeName = Name.get(sootMethod.getName());
    }

    @Override
    public Name getName() {
        return relativeName;
    }

    @Override
    public boolean isPrivate() {
        return sootMethod.isPrivate();
    }

    @Override
    public boolean isProtected() {
        return sootMethod.isProtected();
    }

    @Override
    public boolean isPublic() {
        return sootMethod.isPublic();
    }

    @Override
    public boolean isStatic() {
        return sootMethod.isStatic();
    }

    @Override
    public boolean isAbstract() {
        return sootMethod.isAbstract();
    }

}
 
