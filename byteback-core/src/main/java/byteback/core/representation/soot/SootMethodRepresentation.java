package byteback.core.representation.soot;

import byteback.core.identifier.MethodName;
import byteback.core.representation.MethodRepresentation;
import byteback.core.type.soot.SootType;
import soot.SootMethod;

public class SootMethodRepresentation implements MethodRepresentation {

    private final SootMethod sootMethod;

    private final MethodName name;

    /**
     * Constructor for the Soot method intermediate representation.
     *
     * @param sootMethod The wrapped {@code SootMethod} instance.
     */
    public SootMethodRepresentation(final SootMethod sootMethod) {
        this.sootMethod = sootMethod;
        this.name = new MethodName(sootMethod.getName());
    }

    @Override
    public MethodName getName() {
        return name;
    }

    public SootType getReturnType() {
        return new SootType(sootMethod.getReturnType());
    }

}
 
