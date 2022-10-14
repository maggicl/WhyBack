package byteback.vimp.internal;

import byteback.vimp.Vimp;
import soot.UnitPrinter;
import soot.Value;

public class InvariantStmt extends LogicStmt {

	public InvariantStmt(final Value condition) {
		super(condition);
	}

	public void toString(final UnitPrinter up) {
		up.literal("invariant ");
		getCondition().toString(up);
	}

	@Override
	public InvariantStmt clone() {
		return new InvariantStmt(Vimp.cloneIfNecessary(getCondition()));
	}

}
