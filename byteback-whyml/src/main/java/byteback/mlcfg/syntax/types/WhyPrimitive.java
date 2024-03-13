package byteback.mlcfg.syntax.types;

public enum WhyPrimitive implements WhyType {
	BOOL("jbool", 'Z'),
	BYTE("jbyte", 'B'),
	CHAR("jchar", 'C'),
	SHORT("jshort", 'I'),
	INT("jint", 'I'),
	LONG("jlong", 'J'),
	FLOAT("jfloat", 'F'),
	DOUBLE("jdouble", 'D');

	private final String label;
	private final char accessorScope;

	WhyPrimitive(String typeLabel, char accessorScope) {
		this.label = typeLabel;
		this.accessorScope = accessorScope;
	}

	@Override
	public String getWhyType() {
		return label;
	}

	@Override
	public String getWhyAccessorScope() {
		return Character.toString(accessorScope);
	}
}
