package byteback.mlcfg.syntax;

public enum WhyFunctionKind {
	INSTANCE_METHOD("val", "let rec"), // TODO: change
	STATIC_METHOD("val", "let rec"),
	PURE_FUNCTION("let function", "let rec function"),
	PREDICATE("let predicate", "let rec predicate");

	private final String whyDeclaration;
	private final String recursiveDeclaration;

	WhyFunctionKind(String whyDeclaration, String recursiveDeclaration) {
		this.whyDeclaration = whyDeclaration;
		this.recursiveDeclaration = recursiveDeclaration;
	}

	public String getWhyDeclaration() {
		return whyDeclaration;
	}

	public boolean isSpec() {
		return this == WhyFunctionKind.PURE_FUNCTION || this == WhyFunctionKind.PREDICATE;
	}

	public String getWhyRecDeclaration() {
		return recursiveDeclaration;
	}
}
