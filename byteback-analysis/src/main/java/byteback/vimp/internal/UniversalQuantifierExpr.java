package byteback.vimp.internal;

import byteback.vimp.LogicExprSwitch;
import byteback.vimp.Vimp;
import soot.Local;
import soot.Value;
import soot.util.Chain;
import soot.util.Switch;

public class UniversalQuantifierExpr extends QuantifierExpr {

	public UniversalQuantifierExpr(final Value value, final Chain<Local> freeLocals) {
		super(value, freeLocals);
	}

	public String getSymbol() {
		return "∀";
	}

	@Override
	public void apply(final Switch sw) {
		((LogicExprSwitch) sw).caseUniversalQuantifier(this);
	}

	@Override
	public UniversalQuantifierExpr clone() {
		return new UniversalQuantifierExpr(Vimp.cloneIfNecessary(getValue()), cloneFreeLocals());
	}

}
