package byteback.whyml.syntax.type;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.terminal;

public enum WhyJVMType implements WhyType {
	BOOL("jbool", "Z", "HeapDef.Int", true),
	BYTE("jbyte", "B", "HeapDef.Byte", true),
	CHAR("jchar", "C", "HeapDef.Char", true),
	SHORT("jshort", "S", "HeapDef.Short", true),
	INT("jint", "I", "HeapDef.Int", true),
	LONG("jlong", "J", "HeapDef.Long", true),
	FLOAT("jfloat", "F", "HeapDef.Float", false),
	DOUBLE("jdouble", "D", "HeapDef.Double", false),

	PTR("Ptr.t", "L", null, false),
	// TODO: consider removing. Introduced for exceptions
	UNIT("unit", null, null, false);

	private final String label;
	private final String preludeType;
	private final String accessorScope;
	private final boolean isWholeNumber;

	WhyJVMType(String typeLabel, String accessorScope, String preludeType, boolean isWholeNumber) {
		this.label = typeLabel;
		this.accessorScope = accessorScope;
		this.preludeType = preludeType;
		this.isWholeNumber = isWholeNumber;
	}

	public boolean isMeta() {
		return preludeType == null;
	}

	public boolean isWholeNumber() {
		return this.isWholeNumber;
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
	public SExpr getPreludeType() {
		if (preludeType == null) {
			throw new UnsupportedOperationException(this + " does not have a prelude type");
		}

		return terminal(preludeType);
	}

	@Override
	public void accept(WhyTypeVisitor visitor) {
		visitor.visitPrimitive(this);
	}

	@Override
	public String getWhyAccessorScope() {
		if (accessorScope == null) {
			throw new UnsupportedOperationException(this + " does not have accessors");
		}

		return accessorScope;
	}

	@Override
	public String getDescriptor() {
		if (this == PTR) return WhyReference.OBJECT.getDescriptor();
		if (this == UNIT) return "V";
		return accessorScope;
	}
}
