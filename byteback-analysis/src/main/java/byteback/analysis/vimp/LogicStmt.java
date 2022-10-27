package byteback.analysis.vimp;

import soot.Value;
import soot.jimple.internal.AbstractStmt;

public abstract class LogicStmt extends AbstractStmt {

	private final Value condition;

	public LogicStmt(final Value condition) {
		this.condition = condition;
	}

	public Value getCondition() {
		return condition;
	}

	@Override
	public boolean branches() {
		return false;
	}

	@Override
	public boolean fallsThrough() {
		return false;
	}

}
