package byteback.whyml.syntax.function;

public record WhyFunctionKind(boolean isStatic, Declaration decl, Inline inline) {
	public WhyFunctionKind {
		if (inline != Inline.NEVER && !decl.isSpec()) {
			throw new IllegalArgumentException("A WhyFunction that can be inlined must be a spec function");
		}
	}

	public enum Declaration {
		PROGRAM("let", "let rec", "val"),
		PREDICATE("let ghost predicate", "let rec ghost predicate", "val ghost predicate"),
		FUNCTION("let ghost function", "let rec ghost function", "val ghost function");

		private final String why;
		private final String whyRec;
		private final String whyDecl;

		Declaration(String why, String whyRec, String whyDecl) {
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

	public enum Inline {
		REQUIRED,
		OPTIONAL,
		NEVER;

		public boolean can() {
			return this != NEVER;
		}

		public boolean must() {
			return this == REQUIRED;
		}
	}
}
