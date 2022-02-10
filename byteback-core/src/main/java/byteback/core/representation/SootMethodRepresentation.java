package byteback.core.representation;

import soot.SootMethod;

public class SootMethodRepresentation implements MethodRepresentation {

    private final SootMethod sootMethod;

    /**
     * @param sootMethod The wrapped {@code SootMethod} instance.
     */
    public SootMethodRepresentation(final SootMethod sootMethod) {
        this.sootMethod = sootMethod;
    }

}
 
