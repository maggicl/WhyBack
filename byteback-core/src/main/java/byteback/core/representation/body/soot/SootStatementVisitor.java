package byteback.core.representation.body.soot;

import byteback.core.representation.Visitor;
import soot.Unit;
import soot.jimple.AbstractStmtSwitch;

public abstract class SootStatementVisitor<R> extends AbstractStmtSwitch implements Visitor<Unit, R> {

    public void defaultCase(Object object) {
        caseDefault((Unit) object);
    }
    
}
