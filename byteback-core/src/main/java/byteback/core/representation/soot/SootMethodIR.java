package byteback.core.representation.soot;

import byteback.core.type.Name;
import byteback.core.representation.MethodRepresentation;
import soot.SootMethod;

public class SootMethodIR implements MethodRepresentation {

    private final SootMethod sootMethod;

    private final Name name;

    /**
     * Constructor for the Soot method intermediate representation.
     *
     * @param sootMethod The wrapped {@code SootMethod} instance.
     */
    public SootMethodIR(final SootMethod sootMethod) {
        this.sootMethod = sootMethod;
        this.name = Name.get(sootMethod.getName());
    }

    @Override
    public Name getName() {
        return name;
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
 
