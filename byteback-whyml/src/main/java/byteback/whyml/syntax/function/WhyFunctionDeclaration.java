package byteback.whyml.syntax.function;

public enum WhyFunctionDeclaration {
	PROGRAM("let", "let rec", "val"),
	PREDICATE("let ghost predicate", "let rec ghost predicate", "val ghost predicate"),
	FUNCTION("let ghost function", "let rec ghost function", "val ghost function");

	private final String why;
	private final String whyRec;
	private final String whyDecl;

	WhyFunctionDeclaration(String why, String whyRec, String whyDecl) {
		this.why = why;
		this.whyRec = whyRec;
		this.whyDecl = whyDecl;
	}

	public String toWhy(boolean recursive) {
		return recursive ? whyRec : why;
	}

	public String toWhyDeclaration() {
		return whyDecl;
	}

	public boolean isSpec() {
		return this != PROGRAM;
	}
}
