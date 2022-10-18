package byteback.vimp.internal;

import byteback.vimp.LogicExprSwitch;
import soot.Value;
import soot.ValueBox;
import soot.jimple.NegExpr;
import soot.util.Switch;

public class LogicNotExpr extends AbstractLogicUnopExpr implements LogicExpr, NegExpr {

	public LogicNotExpr(final Value v) {
		super(v);
	}

	public LogicNotExpr(final ValueBox vbox) {
		super(vbox);
	}

	@Override
	public String getSymbol() {
		return "¬";
	}

	@Override
	public void apply(final Switch sw) {
		((LogicExprSwitch) sw).caseLogicNot(this);
	}

	@Override
	public Object clone() {
		return new LogicNotExpr(getOp());
	}

}
