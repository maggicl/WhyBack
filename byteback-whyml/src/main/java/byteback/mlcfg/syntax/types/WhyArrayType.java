package byteback.mlcfg.syntax.types;

public class WhyArrayType implements WhyPtrType {
	private final WhyType baseType;

	public WhyArrayType(WhyType baseType) {
		this.baseType = baseType;
	}

	@Override
	public String getPreludeType() {
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
			return "Type.ArrayOf (%s)".formatted(ptrType.getPreludeType());
		}
	}

	@Override
	public String getWhyAccessorScope() {
		return baseType instanceof WhyPrimitive
				? "R%s".formatted(baseType.getWhyAccessorScope())
				: "RL";
	}
}
