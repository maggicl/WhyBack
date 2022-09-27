package byteback.vimp.internal;

import byteback.vimp.LogicExprSwitch;
import byteback.vimp.Vimp;
import soot.Local;
import soot.Value;
import soot.util.Chain;
import soot.util.Switch;

public class LogicExistsExpr extends QuantifierExpr {

	public LogicExistsExpr(final Value value, final Chain<Local> freeLocals) {
		super(value, freeLocals);
	}

	@Override
	protected String getSymbol() {
		return "∃";
	}

	@Override
	public void apply(final Switch sw) {
		((LogicExprSwitch) sw).caseLogicExistsExpr(this);
	}

	@Override
	public LogicExistsExpr clone() {
		return new LogicExistsExpr(Vimp.cloneIfNecessary(getValue()), cloneFreeLocals());
	}

}
