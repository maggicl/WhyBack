package byteback.mlcfg.syntax.types;

import byteback.mlcfg.identifiers.Identifier;
import java.util.Objects;

public class WhyArrayType implements WhyPtrType {
	public WhyType getBaseType() {
		return baseType;
	}

	private final WhyType baseType;

	public WhyArrayType(WhyType baseType) {
		this.baseType = baseType;
	}

	@Override
	public String getPreludeType(Identifier.FQDN currentScope) {
		if (baseType instanceof WhyPrimitive) {
			return switch ((WhyPrimitive) baseType) {
				case BOOL -> "BoolArray";
				case BYTE -> "ByteArray";
				case CHAR -> "CharArray";
				case SHORT -> "ShortArray";
				case INT -> "IntArray";
				case LONG -> "LongArray";
				case FLOAT -> "FloatArray";
				case DOUBLE -> "DoubleArray";
			};
		} else {
			final WhyPtrType ptrType = (WhyPtrType) baseType;
			return "Type.ArrayOf (%s)".formatted(ptrType.getPreludeType(currentScope));
		}
	}

	@Override
	public void accept(WhyTypeVisitor visitor) {
		visitor.visitArray(this);
	}

	@Override
	public String getWhyAccessorScope() {
		return baseType instanceof WhyPrimitive
				? "R%s".formatted(baseType.getWhyAccessorScope())
				: "RL";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WhyArrayType that = (WhyArrayType) o;
		return Objects.equals(baseType, that.baseType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(baseType);
	}
}
