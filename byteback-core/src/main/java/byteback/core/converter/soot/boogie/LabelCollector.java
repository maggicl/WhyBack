package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.frontend.boogie.ast.Label;
import java.util.HashMap;
import java.util.Map;
import soot.Unit;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;

public class LabelCollector extends SootStatementVisitor<Map<Unit, Label>> {

	private int counter;

	private final Map<Unit, Label> labelTable;

	public LabelCollector() {
		this.counter = 0;
		this.labelTable = new HashMap<>();
	}

	public void branchTo(final Unit target) {
		labelTable.put(target, Prelude.getLabel(++counter));
	}

	@Override
	public void caseIfStmt(final IfStmt ifStatement) {
		branchTo(ifStatement.getTarget());
	}

	@Override
	public void caseGotoStmt(final GotoStmt gotoStatement) {
		branchTo(gotoStatement.getTarget());
	}

	@Override
	public void caseDefault(final Unit unit) {
	}

	@Override
	public Map<Unit, Label> result() {
		return labelTable;
	}

}
