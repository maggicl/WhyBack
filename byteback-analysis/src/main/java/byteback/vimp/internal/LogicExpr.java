package byteback.vimp.internal;

import byteback.LogicType;
import soot.jimple.Expr;

public interface LogicExpr extends Expr {

	@Override
	default LogicType getType() {
		return LogicType.v();
	}

}
