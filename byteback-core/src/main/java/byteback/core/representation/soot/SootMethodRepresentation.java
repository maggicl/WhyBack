package byteback.core.representation.soot;

import byteback.core.representation.MethodRepresentation;
import soot.SootMethod;

public class SootMethodRepresentation implements MethodRepresentation {

    private final SootMethod sootMethod;

    /**
     * Constructor for the
     *
     * @param sootMethod The wrapped {@code SootMethod} instance.
     */
    public SootMethodRepresentation(final SootMethod sootMethod) {
        this.sootMethod = sootMethod;
    }

    /**
     * Gets the name of the method.
     *
     * @return The string name of the method.
     */
    public String getName() {
        return sootMethod.getName();
    }

}
 
