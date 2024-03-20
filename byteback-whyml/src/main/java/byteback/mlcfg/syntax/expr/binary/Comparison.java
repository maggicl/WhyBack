package byteback.mlcfg.syntax.expr.binary;

import byteback.mlcfg.syntax.types.WhyPrimitive;
import byteback.mlcfg.syntax.types.WhyReference;
import byteback.mlcfg.syntax.types.WhyType;
import java.util.Objects;

public final class Comparison implements BinaryOperator {
	private final WhyPrimitive type;
	private final Kind kind;

	private Comparison(WhyPrimitive type, Kind kind) {
		this.type = type;
		this.kind = kind;
	}

	public static Comparison ofPrimitive(WhyPrimitive type, Kind kind) {
		return new Comparison(Objects.requireNonNull(type), Objects.requireNonNull(kind));
	}

	public static Comparison ofObject(Kind kind) {
		if (kind != Kind.EQ && kind != Kind.NE) {
			throw new IllegalArgumentException("object comparison does not have operator " + kind);
		}

		return new Comparison(null, Objects.requireNonNull(kind));
	}

	@Override
	public String opName() {
		return "%sCMP.%s".formatted(
				type == null ? "L" : type.getWhyAccessorScope(),
				kind.operator
		);
	}

	@Override
	public WhyType firstOpType() {
		return type == null ? WhyReference.OBJECT : type;
	}

	@Override
	public WhyType secondOpType() {
		return firstOpType();
	}

	@Override
	public WhyPrimitive returnType() {
		return WhyPrimitive.BOOL;
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
