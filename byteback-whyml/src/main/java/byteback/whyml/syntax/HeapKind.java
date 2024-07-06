package byteback.whyml.syntax;

public enum HeapKind {
	MACHINE("machine"),
	MATH("math");

	private final String name;

	HeapKind(String preludePackageName) {
		this.name = preludePackageName;
	}

	public String getName() {
		return name;
	}
}

