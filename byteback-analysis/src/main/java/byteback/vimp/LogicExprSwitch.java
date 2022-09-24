package byteback.vimp;

import byteback.vimp.internal.LogicAndExpr;
import byteback.vimp.internal.LogicIffExpr;
import byteback.vimp.internal.LogicImpliesExpr;
import byteback.vimp.internal.LogicOrExpr;
import soot.Value;
import soot.util.Switch;

public interface LogicExprSwitch extends Switch {

	default void caseLogicAndExpr(final LogicAndExpr v) {
		defaultCase(v);
	}

	default void caseLogicOrExpr(final LogicOrExpr v) {
		defaultCase(v);
	}

	default void caseLogicImpliesExpr(final LogicImpliesExpr v) {
		defaultCase(v);
	}

	default void caseLogicIffExpr(final LogicIffExpr v) {
		defaultCase(v);
	}

	void defaultCase(final Value v);
	
}
