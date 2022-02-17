package byteback.core.type.soot;

import byteback.core.Visitor;
import soot.TypeSwitch;

/**
 * Base class for a {@link SootType} visitor.
 */
public abstract class SootTypeVisitor extends TypeSwitch implements Visitor {

    /**
     * Default case throwing an exception signaling that the type is not supported
     * by the visitor.
     *
     * @throws UnsupportedOperationException Signaling that the type is not
     *                                       supported.
     */
    @Override
    public void defaultCase(Object object) {
        throw new UnsupportedOperationException();
    }

}
