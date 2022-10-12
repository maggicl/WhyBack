package byteback.core.representation.soot.body;

import byteback.core.representation.Visitor;
import soot.Body;
import soot.Unit;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.IdentityStmt;

public abstract class SootStatementVisitor<R> extends AbstractStmtSwitch<R> implements Visitor<Unit, R> {

	@Override
	public void caseIdentityStmt(final IdentityStmt identity) {
	}

	public void defaultCase(Object object) {
		caseDefault((Unit) object);
	}

	public R visit(final Unit unit) {
		unit.apply(this);

		return result();
	}

	public R visit(final Body body) {

		for (Unit unit : body.getUnits()) {
			unit.apply(this);
		}

		return result();
	}

}
