package byteback.analysis.vimp;

import byteback.analysis.LogicStmtVisitor;
import byteback.analysis.Vimp;
import soot.UnitPrinter;
import soot.Value;
import soot.util.Switch;

public class AssumptionStmt extends LogicStmt {

	public AssumptionStmt(final Value condition) {
		super(condition);
	}

	@Override
	public void apply(final Switch sw) {
		if (sw instanceof LogicStmtVisitor<?> visitor) {
			visitor.caseAssumptionStmt(this);
		}
	}

	@Override
	public AssumptionStmt clone() {
		return new AssumptionStmt(Vimp.cloneIfNecessary(getCondition()));
	}

	@Override
	public void toString(final UnitPrinter up) {
		up.literal("assume ");
		getCondition().toString(up);
	}

	@Override
	public String toString() {
		return "assume " + getCondition().toString();
	}
}
