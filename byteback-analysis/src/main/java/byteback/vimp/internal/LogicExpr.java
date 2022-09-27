package byteback.vimp.internal;

import byteback.vimp.LogicType;
import soot.jimple.Expr;

public interface LogicExpr extends Expr {

	@Override
	default LogicType getType() {
		return LogicType.v();
	}

}
