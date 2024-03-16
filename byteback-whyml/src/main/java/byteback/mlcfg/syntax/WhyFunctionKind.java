package byteback.mlcfg.syntax;

public enum WhyFunctionKind {
	INSTANCE_METHOD("val"),
	STATIC_METHOD("val"),
	PURE_FUNCTION("let function"),
	PREDICATE("let predicate");

	private final String whyDeclaration;

	WhyFunctionKind(String whyDeclaration) {
		this.whyDeclaration = whyDeclaration;
	}

	public String getWhyDeclaration() {
		return whyDeclaration;
	}
}
