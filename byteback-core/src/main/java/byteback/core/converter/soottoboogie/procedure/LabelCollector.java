package byteback.core.converter.soottoboogie.procedure;

import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.representation.soot.body.SootBody;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.frontend.boogie.ast.Label;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import soot.Unit;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;

public class LabelCollector extends SootStatementVisitor<Map<Unit, Label>> {

	private int counter;

	private final Map<Unit, Label> labels;

	public LabelCollector() {
		this.counter = 0;
		this.labels = new HashMap<>();
	}

	public boolean hasLabel(final Unit unit) {
		return labels.containsKey(unit);
	}

	public Optional<Label> getLabel(final Unit unit) {
		return Optional.ofNullable(labels.get(unit));
	}

	public void branchTo(final Unit target) {
		labels.put(target, Prelude.makeLabel(++counter));
	}

	public void collect(final SootBody body) {
		visit(body);
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
		return labels;
	}

}
