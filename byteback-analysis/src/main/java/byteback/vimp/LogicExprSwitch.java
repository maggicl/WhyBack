package byteback.vimp;

import soot.Value;
import soot.jimple.ExprSwitch;

public abstract class LogicExprSwitch implements ExprSwitch, LogicExprVisitor {

	@Override
	public void defaultCase(final Object object) {
		defaultCase((Value) object);
	}

}
