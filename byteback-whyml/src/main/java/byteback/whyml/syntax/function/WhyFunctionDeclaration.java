package byteback.whyml.syntax.function;

import java.util.StringJoiner;

public enum WhyFunctionDeclaration {
	PROGRAM("cfg", null),
	PREDICATE(null, "ghost predicate"),
	FUNCTION(null, "ghost function");

	private final String implOnlyToken;
	private final String token;

	WhyFunctionDeclaration(String implOnlyToken, String token) {
		this.implOnlyToken = implOnlyToken;
		this.token = token;
	}

	private static String build(String... tokens) {
		final StringJoiner s = new StringJoiner(" ");

		for (final String token : tokens) {
			if (token != null) {
				s.add(token);
			}
		}

		return s.toString();
	}

	public String toWhy(boolean useWith, boolean recursive) {
		return build(
				useWith ? "with" : "let",
				!useWith && recursive ? "rec" : null,
				implOnlyToken,
				token
		);
	}

	public String toWhyDeclaration() {
		return build("val", token);
	}

	public boolean isSpec() {
		return this != PROGRAM;
	}

	public boolean isProgram() {
		return this == PROGRAM;
	}
}
