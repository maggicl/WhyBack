package byteback.vimp.internal;

import byteback.vimp.LogicExprSwitch;
import byteback.vimp.Vimp;
import soot.Value;
import soot.util.Switch;

public class LogicImpliesExpr extends AbstractLogicBinopExpr implements LogicExpr {

	public LogicImpliesExpr(final Value op1, final Value op2) {
		super(op1, op2);
	}

	public String getSymbol() {
		return " → ";
	}

	public LogicAndExpr clone() {
		return new LogicAndExpr(Vimp.cloneIfNecessary(getOp1()), Vimp.cloneIfNecessary(getOp2()));
	}

  @Override
  public void apply(final Switch sw) {
    ((LogicExprSwitch) sw).caseLogicImpliesExpr(this);
  }

}
