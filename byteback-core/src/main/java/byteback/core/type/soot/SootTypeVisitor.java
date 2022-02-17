package byteback.core.type.soot;

import byteback.core.Visitor;
import soot.AnySubType;
import soot.TypeSwitch;

public abstract class SootTypeVisitor extends TypeSwitch implements Visitor {

    @Override
    public final void caseAnySubType(final AnySubType type) {
        throw new UnsupportedOperationException();
    }

}
