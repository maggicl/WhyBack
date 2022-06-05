package byteback.core.representation.soot.body;

import byteback.core.representation.Visitor;
import soot.Unit;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.IdentityStmt;

public abstract class SootStatementVisitor<R> extends AbstractStmtSwitch implements Visitor<Unit, R> {

	@Override
	public void caseIdentityStmt(final IdentityStmt identity) {
	}

	public void defaultCase(Object object) {
		caseDefault((Unit) object);
	}

}
