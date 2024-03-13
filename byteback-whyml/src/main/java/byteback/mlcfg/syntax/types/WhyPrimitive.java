package byteback.mlcfg.syntax.types;

public enum WhyPrimitive implements WhyType {
	BOOL("jbool", 'Z', "HeapDef.Int"),
	BYTE("jbyte", 'B', "HeapDef.Byte"),
	CHAR("jchar", 'C', "HeapDef.Char"),
	SHORT("jshort", 'I', "HeapDef.Short"),
	INT("jint", 'I', "HeapDef.Int"),
	LONG("jlong", 'J', "HeapDef.Long"),
	FLOAT("jfloat", 'F', "HeapDef.Float"),
	DOUBLE("jdouble", 'D', "HeapDef.Double");

	private final String label;
	private final String preludeType;
	private final char accessorScope;

	WhyPrimitive(String typeLabel, char accessorScope, String preludeType) {
		this.label = typeLabel;
		this.accessorScope = accessorScope;
		this.preludeType = preludeType;
	}

	@Override
	public String getWhyType() {
		return label;
	}

	@Override
	public String getPreludeType() {
		return preludeType;
	}

	@Override
	public String getWhyAccessorScope() {
		return Character.toString(accessorScope);
	}
}
