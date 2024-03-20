package byteback.mlcfg.syntax.types;

import java.util.Objects;

public record WhyArrayType(WhyType baseType) implements WhyPtrType {
	public WhyArrayType {
		// noinspection SuspiciousMethodCalls
		if (WhyJVMType.META_TYPES.contains(baseType)) {
			throw new IllegalArgumentException("base type of array cannot be generic reference JVM type");
		}
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
				default -> throw new IllegalStateException("unreachable");
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
}
