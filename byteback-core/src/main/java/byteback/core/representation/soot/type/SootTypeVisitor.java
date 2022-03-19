package byteback.core.representation.soot.type;

import byteback.core.representation.Visitor;
import soot.Type;
import soot.TypeSwitch;

/**
 * Base class for a {@link SootType} visitor.
 */
public abstract class SootTypeVisitor<R> extends TypeSwitch implements Visitor<Type, R> {

    @Override
    public abstract void caseDefault(Type type);

    @Override
    public void defaultCase(Type type) {
        caseDefault(type);
    }

}
