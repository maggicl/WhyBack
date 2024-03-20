package byteback.mlcfg.syntax.types;

import java.util.Objects;

public class WhyArrayType implements WhyPtrType {
	public WhyType getBaseType() {
		return baseType;
	}

	private final WhyType baseType;

	public WhyArrayType(WhyType baseType) {
		if (baseType == WhyJVMType.PTR) {
			throw new IllegalArgumentException("base type of array cannot be generic reference JVM type");
		}

		this.baseType = baseType;
	}

	@Override
	public String getPreludeType() {
		if (baseType instanceof WhyJVMType) {
			return switch ((WhyJVMType) baseType) {
				case BOOL -> "BoolArray";
				case BYTE -> "ByteArray";
				case CHAR -> "CharArray";
				case SHORT -> "ShortArray";
				case INT -> "IntArray";
				case LONG -> "LongArray";
				case FLOAT -> "FloatArray";
				case DOUBLE -> "DoubleArray";
				case PTR -> throw new IllegalStateException("unreachable");
			};
		} else {
			final WhyPtrType ptrType = (WhyPtrType) baseType;
			return "Type.ArrayOf (%s)".formatted(ptrType.getPreludeType());
		}
	}

	@Override
	public void accept(WhyTypeVisitor visitor) {
		visitor.visitArray(this);
	}

	@Override
	public String getWhyAccessorScope() {
		return "R%s".formatted(baseType.jvm().getWhyAccessorScope());
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
