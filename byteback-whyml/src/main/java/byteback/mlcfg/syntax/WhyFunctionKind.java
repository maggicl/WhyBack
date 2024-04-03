package byteback.mlcfg.syntax;

public enum WhyFunctionKind {
	INSTANCE_METHOD("val"),
	STATIC_METHOD("val"),
	PURE_FUNCTION("let rec function"), // forcing 'rec' here has it has no effect on non-recursive functions
	PREDICATE("let rec predicate");

	private final String whyDeclaration;

	WhyFunctionKind(String whyDeclaration) {
		this.whyDeclaration = whyDeclaration;
	}

	public String getWhyDeclaration() {
		return whyDeclaration;
	}

	public boolean isSpec() {
		return this == WhyFunctionKind.PURE_FUNCTION || this == WhyFunctionKind.PREDICATE;
	}
}
