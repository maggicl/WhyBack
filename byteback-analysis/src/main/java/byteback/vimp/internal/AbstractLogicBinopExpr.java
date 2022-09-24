package byteback.vimp.internal;

import byteback.vimp.Vimp;
import soot.Value;
import soot.jimple.internal.AbstractBinopExpr;

public abstract class AbstractLogicBinopExpr extends AbstractBinopExpr {

	public AbstractLogicBinopExpr(final Value op1, final Value op2) {
		super(Vimp.v().newArgBox(op1), Vimp.v().newArgBox(op2));
	}

}
