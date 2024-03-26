package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyJVMType;
import java.util.Locale;

public class PrimitiveCastExpression implements Expression {
	private final Expression op;
	private final WhyJVMType targetType;

	public PrimitiveCastExpression(Expression op, WhyJVMType type) {
		if (op.type().isMeta()) {
			throw new IllegalArgumentException("inner expression of primitive cast operation must have primitive type");
		}

		if (type.isMeta()) {
			throw new IllegalArgumentException("target type of primitive cast operation must be primitive");
		}

		this.op = op;
		this.targetType = type;
	}

	private static String getCastTypeName(WhyJVMType type) {
		return type.getWhyAccessorScope().toLowerCase(Locale.ROOT);
	}

	@Override
	public String toWhy() {
		final WhyJVMType sourceType = op.type();
		if (sourceType.isWholeNumber() || targetType.isWholeNumber()) {
			return "(int2%s (%s2int (%s)))".formatted(
					getCastTypeName(targetType),
					getCastTypeName(sourceType),
					op.toWhy()
			);
		} else {
			return "(%s2%s (%s))".formatted(
					getCastTypeName(sourceType),
					getCastTypeName(targetType),
					op.toWhy()
			);
		}
	}

	@Override
	public WhyJVMType type() {
		return targetType;
	}
}
