package byteback.mlcfg.syntax;

public enum WhyFunctionKind {

	/**
	 * An instance method, program code
	 */
	INSTANCE_METHOD("val", "let rec"), // TODO: change

	/**
	 * A static method, program code
	 */
	STATIC_METHOD("val", "let rec"),

	/**
	 * A pure specification function
	 */
	PURE("let function", "let rec function"),

	/**
	 * A pure specification predicate
	 */
	PURE_PREDICATE("let predicate", "let rec predicate"),

	/**
	 * Body of a precondition or postcondition. To be inlined in a <code>requires</code> or <code>ensures</code> clause.
	 */
	PREDICATE(null, null);

	private final String whyDeclaration;
	private final String recursiveDeclaration;

	WhyFunctionKind(String whyDeclaration, String recursiveDeclaration) {
		this.whyDeclaration = whyDeclaration;
		this.recursiveDeclaration = recursiveDeclaration;
	}

	public boolean hasDeclaration() {
		return this != PREDICATE;
	}

	public String getWhyDeclaration() {
		if (whyDeclaration == null) throw new UnsupportedOperationException();
		return whyDeclaration;
	}

	public boolean isSpec() {
		return this == WhyFunctionKind.PURE
				|| this == WhyFunctionKind.PURE_PREDICATE
				|| this == WhyFunctionKind.PREDICATE;
	}

	public String getWhyRecDeclaration() {
		if (recursiveDeclaration == null) throw new UnsupportedOperationException();
		return recursiveDeclaration;
	}
}
