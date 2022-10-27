package byteback.analysis;

import byteback.analysis.vimp.AssertionStmt;
import byteback.analysis.vimp.AssumptionStmt;
import byteback.analysis.vimp.InvariantStmt;
import soot.Unit;

public interface LogicStmtVisitor {

	default void caseAssertionStmt(final AssertionStmt s) {
		defaultCase(s);
	}

	default void caseAssumptionStmt(final AssumptionStmt s) {
		defaultCase(s);
	}

	default void caseInvariantStmt(final InvariantStmt s) {
		defaultCase(s);
	}

	default void defaultCase(final Unit s) {
	}

}
