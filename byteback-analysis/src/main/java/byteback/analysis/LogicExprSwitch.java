package byteback.analysis;

import soot.Value;
import soot.jimple.ExprSwitch;

public interface LogicExprSwitch extends ExprSwitch, LogicExprVisitor {

	@Override
	default void defaultCase(final Object object) {
		defaultCase((Value) object);
	}

}
