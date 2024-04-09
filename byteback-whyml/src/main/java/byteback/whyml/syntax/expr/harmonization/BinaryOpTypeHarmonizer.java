package byteback.whyml.syntax.expr.harmonization;

import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.type.WhyJVMType;

public final class BinaryOpTypeHarmonizer {
	private BinaryOpTypeHarmonizer() {
	}

	public static HarmonizationResult harmonize(final Expression firstArg, final Expression secondArg) {
		final WhyJVMType firstOpType = firstArg.type();
		final WhyJVMType secondOpType = secondArg.type();

		if (firstOpType == secondOpType) {
			return new HarmonizationResult(firstOpType, firstArg, secondArg);
		}

		if (firstOpType != WhyJVMType.INT && secondOpType != WhyJVMType.INT) {
			throw new UnsupportedOperationException("can only harmonize when types are different if one type is INT");
		} else {
			return new HarmonizationResult(firstOpType == WhyJVMType.INT ? secondOpType : firstOpType, firstArg, secondArg);
		}
	}

}
