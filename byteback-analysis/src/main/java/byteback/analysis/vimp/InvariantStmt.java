package byteback.analysis.vimp;

import byteback.analysis.LogicStmtVisitor;
import byteback.analysis.Vimp;
import soot.UnitPrinter;
import soot.Value;
import soot.util.Switch;

public class InvariantStmt extends LogicStmt {

	public boolean isInferredAutomatically() {
		return inferredAutomatically;
	}

	final boolean inferredAutomatically;

	public InvariantStmt(final Value condition, boolean inferredAutomatically) {
		super(condition);
		this.inferredAutomatically = inferredAutomatically;
	}

	@Override
	public void apply(final Switch sw) {
		if (sw instanceof LogicStmtVisitor<?> visitor) {
			visitor.caseInvariantStmt(this);
		}
	}

	@Override
	public InvariantStmt clone() {
		return new InvariantStmt(Vimp.cloneIfNecessary(getCondition()), inferredAutomatically);
	}

	@Override
	public void toString(final UnitPrinter up) {
		up.literal("invariant ");
		getCondition().toString(up);
	}

	@Override
	public String toString() {
		return "invariant " + getCondition().toString();
	}
}
