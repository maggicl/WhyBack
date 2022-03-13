package byteback.core.representation.type.soot;

import byteback.core.representation.Visitor;
import soot.Type;
import soot.TypeSwitch;

/**
 * Base class for a {@link SootType} visitor.
 */
public abstract class SootTypeVisitor<R> extends TypeSwitch implements Visitor<Type, R> {

    @Override
    public abstract void caseDefault(Type type);

}
