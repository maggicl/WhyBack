package byteback.core.representation.soot;

import byteback.core.type.Name;
import byteback.core.representation.MethodRepresentation;
import byteback.core.type.soot.SootType;
import soot.SootMethod;

public class SootMethodRepresentation implements MethodRepresentation<SootType> {

    private final SootMethod sootMethod;

    private final Name name;

    /**
     * Constructor for the Soot method intermediate representation.
     *
     * @param sootMethod The wrapped {@code SootMethod} instance.
     */
    public SootMethodRepresentation(final SootMethod sootMethod) {
        this.sootMethod = sootMethod;
        this.name = Name.get(sootMethod.getName());
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public SootType getReturnType() {
        return new SootType(sootMethod.getReturnType());
    }

    @Override
    public boolean isPrivate() {
        return sootMethod.isPrivate();
    }

    @Override
    public boolean isPublic() {
        return sootMethod.isPublic();
    }

    @Override
    public boolean isProtected() {
        return sootMethod.isProtected();
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
 
