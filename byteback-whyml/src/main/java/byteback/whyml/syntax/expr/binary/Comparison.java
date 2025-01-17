package byteback.whyml.syntax.expr.binary;

import byteback.whyml.syntax.type.WhyJVMType;
import java.util.Objects;

public final class Comparison implements BinaryOperator {
	private final WhyJVMType type;
	private final Kind kind;

	public Comparison(WhyJVMType type, Kind kind) {
		if (type == WhyJVMType.PTR && (kind != Kind.EQ && kind != Kind.NE)) {
			throw new IllegalArgumentException("object comparison does not have operator " + kind);
		}

		if (type == WhyJVMType.UNIT) {
			throw new IllegalArgumentException("cannot use unit type in comparison");
		}

		this.type = Objects.requireNonNull(type);
		this.kind = Objects.requireNonNull(kind);
	}

	@Override
	public String opName() {
		return "%sCMP.%s".formatted(
				type.getWhyAccessorScope(),
				kind.operator
		);
	}

	@Override
	public WhyJVMType firstOpType() {
		return type;
	}

	@Override
	public WhyJVMType secondOpType() {
		return firstOpType();
	}

	@Override
	public WhyJVMType returnType() {
		return WhyJVMType.BOOL;
	}

	@Override
	public boolean isInfix() {
		return false;
	}

	public enum Kind {
		EQ("eq"),
		NE("ne"),
		GT("gt"),
		LT("lt"),
		GE("ge"),
		LE("le");

		private final String operator;

		Kind(String operator) {
			this.operator = operator;
		}
	}
}
