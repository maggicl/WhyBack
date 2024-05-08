package byteback.whyml.syntax.type;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import java.util.Objects;

public record WhyArrayType(WhyType baseType) implements WhyPtrType {
	public WhyArrayType {
		if (baseType instanceof WhyJVMType && ((WhyJVMType) baseType).isMeta()) {
			throw new IllegalArgumentException("base type of array cannot be a meta type");
		}
	}

	@Override
	public SExpr getPreludeType() {
		if (baseType instanceof WhyJVMType) {
			return terminal(switch ((WhyJVMType) baseType) {
				case BOOL -> "BoolArray";
				case BYTE -> "ByteArray";
				case CHAR -> "CharArray";
				case SHORT -> "ShortArray";
				case INT -> "IntArray";
				case LONG -> "LongArray";
				case FLOAT -> "FloatArray";
				case DOUBLE -> "DoubleArray";
				default -> throw new IllegalStateException("unreachable");
			});
		} else {
			final WhyPtrType ptrType = (WhyPtrType) baseType;
			return prefix("Type.ArrayOf", ptrType.getPreludeType());
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
	public String getDescriptor() {
		return "R" + baseType.getDescriptor();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WhyArrayType that = (WhyArrayType) o;
		return Objects.equals(baseType, that.baseType);
	}
}
