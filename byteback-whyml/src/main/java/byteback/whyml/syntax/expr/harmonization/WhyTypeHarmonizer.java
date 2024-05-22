package byteback.whyml.syntax.expr.harmonization;

import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;

public final class WhyTypeHarmonizer {
	private WhyTypeHarmonizer() {
	}

	public static HarmonizationResult harmonizeBinaryExpression(final Expression firstArg, final Expression secondArg) {
		final WhyJVMType firstOpType = firstArg.type();
		final WhyJVMType secondOpType = secondArg.type();

		if (firstOpType == secondOpType) {
			return new HarmonizationResult(firstOpType, firstArg, secondArg);
		}

		if (firstOpType != WhyJVMType.INT && secondOpType != WhyJVMType.INT) {
			throw new IllegalArgumentException("can only harmonize when types are different if one type is INT");
		} else {
			return new HarmonizationResult(firstOpType == WhyJVMType.INT ? secondOpType : firstOpType, firstArg, secondArg);
		}
	}

	public static Expression harmonizeExpression(final WhyType callType, final Expression argument) {
		final WhyJVMType callJVMType = callType.jvm();
		final WhyJVMType argType = argument.type();

		if (callJVMType == argType) {
			return argument;
		}

		if (callJVMType != WhyJVMType.INT && argType != WhyJVMType.INT) {
			throw new IllegalArgumentException("expected type %s, given JVM type: %s".formatted(callType, argument.type()));
		} else {
			return argument.asType(callJVMType);
		}
	}

}
