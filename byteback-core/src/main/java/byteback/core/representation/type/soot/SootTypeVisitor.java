package byteback.core.representation.type.soot;

import byteback.core.representation.Visitor;
import soot.Type;
import soot.TypeSwitch;

/**
 * Base class for a {@link SootType} visitor.
 */
public abstract class SootTypeVisitor extends TypeSwitch implements Visitor<Type> {

    @SuppressWarnings("deprecation")
    @Override
    public abstract void caseDefault(Type type);

}