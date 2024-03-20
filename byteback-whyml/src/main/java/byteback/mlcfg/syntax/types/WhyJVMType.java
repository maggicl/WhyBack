package byteback.mlcfg.syntax.types;

import java.util.EnumSet;

public enum WhyJVMType implements WhyType {
	BOOL("jbool", "Z", "HeapDef.Int"),
	BYTE("jbyte", "B", "HeapDef.Byte"),
	CHAR("jchar", "C", "HeapDef.Char"),
	SHORT("jshort", "I", "HeapDef.Short"),
	INT("jint", "I", "HeapDef.Int"),
	PTR("Ptr.t", "L", null),
	UNIT("unit", null, null), // TODO: consider removing. Introduced for exceptions
	LONG("jlong", "J", "HeapDef.Long"),
	FLOAT("jfloat", "F", "HeapDef.Float"),
	DOUBLE("jdouble", "D", "HeapDef.Double");

	public static final EnumSet<WhyJVMType> META_TYPES = EnumSet.of(UNIT, PTR);

	private final String label;
	private final String preludeType;
	private final String accessorScope;

	WhyJVMType(String typeLabel, String accessorScope, String preludeType) {
		this.label = typeLabel;
		this.accessorScope = accessorScope;
		this.preludeType = preludeType;
	}

	@Override
	public String getWhyType() {
		return label;
	}

	@Override
	public WhyJVMType jvm() {
		return this;
	}

	@Override
	public String getPreludeType() {
		if (preludeType == null) {
			throw new UnsupportedOperationException(this + " does not have a prelude type");
		}

		return preludeType;
	}

	@Override
	public void accept(WhyTypeVisitor visitor) {
		visitor.visitPrimitive(this);
	}

	@Override
	public String getWhyAccessorScope() {
		if (preludeType == null) {
			throw new UnsupportedOperationException(this + " does not have accessors");
		}

		return accessorScope;
	}
}
