package byteback.analysis;

import soot.Body;
import soot.Unit;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.IdentityStmt;

public abstract class JimpleStmtSwitch<R> extends AbstractStmtSwitch<R> implements LogicStmtSwitch, Visitor<Unit, R> {

	@Override
	public void caseIdentityStmt(final IdentityStmt identity) {
	}

	@Override
	public void caseDefault(final Unit unit) {
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
