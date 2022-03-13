package byteback.core.representation.body.soot;

import byteback.core.representation.Visitor;
import soot.Value;
import soot.jimple.AbstractJimpleValueSwitch;

public abstract class SootExpressionVisitor<R> extends AbstractJimpleValueSwitch implements Visitor<Value, R> {

    public void defaultCase(Object object) {
        caseDefault((Value) object);
    }

}
