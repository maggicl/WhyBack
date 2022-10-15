package byteback.vimp.internal;

import byteback.vimp.LogicExprVisitor;
import byteback.vimp.Vimp;
import soot.Value;
import soot.jimple.OrExpr;
import soot.util.Switch;

public class LogicOrExpr extends AbstractLogicBinopExpr implements LogicExpr, OrExpr {

	public LogicOrExpr(final Value op1, final Value op2) {
		super(op1, op2);
	}

	public String getSymbol() {
		return " ∨ ";
	}

	public LogicOrExpr clone() {
		return new LogicOrExpr(Vimp.cloneIfNecessary(getOp1()), Vimp.cloneIfNecessary(getOp2()));
	}

  @Override
  public void apply(final Switch sw) {
    ((LogicExprVisitor) sw).caseLogicOrExpr(this);
  }

}
