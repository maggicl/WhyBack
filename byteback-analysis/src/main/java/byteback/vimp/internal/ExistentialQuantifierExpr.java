package byteback.vimp.internal;

import byteback.vimp.LogicExprSwitch;
import byteback.vimp.Vimp;
import soot.Local;
import soot.Value;
import soot.util.Chain;
import soot.util.Switch;

public class ExistentialQuantifierExpr extends QuantifierExpr {

	public ExistentialQuantifierExpr(final Value value, final Chain<Local> freeLocals) {
		super(value, freeLocals);
	}

	public String getSymbol() {
		return "∃";
	}

	@Override
	public void apply(final Switch sw) {
		((LogicExprSwitch) sw).caseExistentialQuantifier(this);
	}

	@Override
	public ExistentialQuantifierExpr clone() {
		return new ExistentialQuantifierExpr(Vimp.cloneIfNecessary(getValue()), cloneFreeLocals());
	}

}
