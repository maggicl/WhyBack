package byteback.core.representation.body.soot;

import byteback.core.representation.Visitor;
import soot.Unit;
import soot.jimple.AbstractStmtSwitch;

public abstract class SootStatementVisitor extends AbstractStmtSwitch implements Visitor<Unit> {

}