package byteback.vimp.internal;

import byteback.vimp.LogicExprSwitch;
import byteback.vimp.Vimp;
import soot.Local;
import soot.Value;
import soot.util.Chain;
import soot.util.Switch;

public class LogicForallExpr extends QuantifierExpr {

	public LogicForallExpr(final Value value, final Chain<Local> freeLocals) {
		super(value, freeLocals);
	}

	@Override
	protected String getSymbol() {
		return "∀";
	}

	@Override
	public void apply(final Switch sw) {
		((LogicExprSwitch) sw).caseLogicForallExpr(this);
	}

	@Override
	public LogicForallExpr clone() {
		return new LogicForallExpr(Vimp.cloneIfNecessary(getValue()), cloneFreeLocals());
	}

}
