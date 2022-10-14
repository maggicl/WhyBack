package byteback.vimp.internal;

import byteback.vimp.Vimp;
import soot.UnitPrinter;
import soot.Value;

public class AssertionStmt extends LogicStmt {

	public AssertionStmt(final Value condition) {
		super(condition);
	}

	public void toString(final UnitPrinter up) {
		up.literal("assert ");
		getCondition().toString(up);
	}

	@Override
	public AssertionStmt clone() {
		return new AssertionStmt(Vimp.cloneIfNecessary(getCondition()));
	}

}
