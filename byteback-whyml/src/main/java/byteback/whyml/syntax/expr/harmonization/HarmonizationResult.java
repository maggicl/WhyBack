package byteback.whyml.syntax.expr.harmonization;

import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.type.WhyJVMType;

public final class HarmonizationResult {
	private final WhyJVMType type;
	private final Expression firstOp;
	private final Expression secondOp;

	HarmonizationResult(WhyJVMType type, Expression firstOp, Expression secondOp) {
		this.type = type;
		this.firstOp = firstOp;
		this.secondOp = secondOp;
	}

	public WhyJVMType getType() {
		return type;
	}

	public Expression getFirstOp() {
		return firstOp.asType(type);
	}

	public Expression getSecondOp() {
		return secondOp.asType(type);
	}
}
