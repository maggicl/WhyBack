package byteback.vimp.internal;

import byteback.vimp.Vimp;
import soot.UnitPrinter;
import soot.Value;

public class AssumptionStmt extends LogicStmt {

	public AssumptionStmt(final Value condition) {
		super(condition);
	}

	public void toString(final UnitPrinter up) {
		up.literal("assume ");
		getCondition().toString(up);
	}

	@Override
	public AssumptionStmt clone() {
		return new AssumptionStmt(Vimp.cloneIfNecessary(getCondition()));
	}

}
