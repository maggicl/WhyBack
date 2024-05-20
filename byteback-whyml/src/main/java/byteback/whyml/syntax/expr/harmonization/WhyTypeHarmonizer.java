package byteback.whyml.syntax.expr.harmonization;

import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.type.WhyArrayType;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyReference;
import byteback.whyml.syntax.type.WhyType;

public final class WhyTypeHarmonizer {
	private WhyTypeHarmonizer() {
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

	public static Expression harmonizeCall(int argNumber, final WhyType callType, final Expression argument) {
		final WhyJVMType callJVMType = callType.jvm();
		final WhyJVMType argType = argument.type();

		if (callJVMType == argType) {
			return argument;
		}

		if (callJVMType != WhyJVMType.INT && argType != WhyJVMType.INT) {
			throw new UnsupportedOperationException("argument %d must be of type %s, given JVM type: %s"
					.formatted(argNumber, callType, argument.type()));
		} else {
			return argument.asType(callJVMType);
		}
	}

}
