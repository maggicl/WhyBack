package byteback.vimp.internal;

import byteback.vimp.LogicExprSwitch;
import soot.jimple.Constant;
import soot.util.Switch;

public class LogicConstant extends Constant implements LogicExpr {

	public final boolean value;

	private static LogicConstant falseConstant = new LogicConstant(false);

	private static LogicConstant trueConstant = new LogicConstant(true);

	private LogicConstant(final boolean value) {
		this.value = value;
	}

	public static LogicConstant v(boolean value) {
		return value ? trueConstant : falseConstant;
	}

	@Override
	public void apply(final Switch sw) {
		((LogicExprSwitch) sw).caseLogicConstant(this);
	}

	public boolean getValue() {
		return value;
	}

}
