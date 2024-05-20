package byteback.whyml.identifiers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Converts JVM identifiers in the restricted WhyML identifier space
 */
public class IdentifierEscaper {

	/**
	 * Used by the prelude to use a separate identifier class for reserved variables (e.g. heap)
	 * TODO: check if a constant is needed
	 */
	public static final String PRELUDE_RESERVED = "'8";
	/**
	 * Separates the method name from the parameter descriptor section, and the parameter descriptor section from the
	 * return type descriptor
	 */
	public static final String DESCRIPTOR_SECTION_SEPARATOR = "'7";
	/**
	 * Separates scopes in a descriptor class name for a parameter
	 */
	static final String DESCRIPTOR_SEPARATOR = "'5";
	/**
	 * Terminates a descriptor class name
	 */
	static final String DESCRIPTOR_END = "'6";
	/**
	 * escapes non-alphanumerics with codepoint <= 255
	 */
	private static final String ESCAPE_LOW_CP = "'0";
	/**
	 * escapes non-alphanumerics with codepoint > 255
	 */
	private static final String ESCAPE_HIGH_CP = "'1";
	/**
	 * Added at the start of the identifier when the first letter must be lowercase
	 */
	private static final String FORCE_LOWERCASE = "i'2";
	/**
	 * Added at the start of the identifier when the first letter must be uppercase
	 */
	private static final String FORCE_UPPERCASE = "I'2";
	/**
	 * Added at the end of the identifier if the identifier matches with one of the reserved identifiers
	 */
	private static final String RESERVED = "'3";
	/**
	 * Separates scopes in a function name
	 */
	private static final String SCOPE_SEPARATOR = "'4";
	/**
	 * Denotes a prime
	 */
	private static final String PRIME = "'9";

	// source: src/parser/handcrafted.messages, lines 18-21
	private static final Set<String> RESERVED_L_KEYWORDS = Set.of("val", "user", "type", "theory", "scope", "predicate",
			"module", "meta", "let", "lemma", "inductive", "import", "goal", "function", "exception", "eof", "end",
			"constant", "coinductive", "clone", "axiom", "as", "old", "to", "returns", "ensures", "requires", "raises");

	private static final String LOCAL_VARIABLE_PREFIX = "lv_";
	private static final String PARAM_PREFIX = "p_";
	private final CaseInverter caseInverter;

	public IdentifierEscaper(CaseInverter caseInverter) {
		this.caseInverter = caseInverter;
	}

	private static String escapeChar(int e) {
		if (e == '\'') {
			return PRIME;
		}

		if (Identifier.isLegalChar(e)) {
			return Character.toString(e);
		}

		return e <= 255 ? String.format("%s%02X", ESCAPE_LOW_CP, e) : String.format("%s%04X", ESCAPE_HIGH_CP, e);
	}

	/**
	 * Given a JVM identifier made of Unicode codepoints, returns a valid WhyML identifier for the specified identifier
	 * class
	 *
	 * @param input the JVM identifier
	 * @param type  the WhyML identifier class
	 * @return the valid WhyML identifier for the chosen class
	 */
	private String escape(String input, IdentifierClass type) {
		final int firstChar = input.codePointAt(0);
		final int firstCharBiased;

		// invert the letter case for uident identifiers as we expect package names, which usually start with a lowercase
		// letter
		if (type == IdentifierClass.UIDENT) {
			firstCharBiased = caseInverter.invertCase(firstChar);
		} else {
			firstCharBiased = firstChar;
		}

		// use the first code point as-is if valid for the current identifier class, otherwise escape with the
		// matching force-case escape sequence
		final String firstIdentifierChar = type.validStart(firstCharBiased) ?
				Character.toString(firstCharBiased) :
				String.format("%s%s", type.getForceEscaper(), escapeChar(firstChar));

		// and now map the remaining characters
		return input.codePoints().skip(1)
				.mapToObj(IdentifierEscaper::escapeChar)
				.collect(Collectors.joining("", firstIdentifierChar, ""));
	}

	public Identifier.L escapeLocalVariable(String input) {
		// no need to check for reserved keywords thanks to prefix
		return new Identifier.L(escape(LOCAL_VARIABLE_PREFIX + input, IdentifierClass.LIDENT));
	}

	public Identifier.L escapeParam(String input) {
		// no need to check for reserved keywords thanks to prefix
		return new Identifier.L(escape(PARAM_PREFIX + input, IdentifierClass.LIDENT));
	}

	public Identifier.L escapeMethod(Identifier.FQDN clazz, String methodName, String descriptor) {
		final List<Identifier.U> ids = clazz.getIdentifiers();
		final int last = ids.size() - 1;

		String classPrefix = Stream.concat(
						ids.stream()
								.limit(last)
								.map(Identifier.U::toString)
								.map(caseInverter::invertCase),
						Stream.of(ids.get(last).toString()))
				.collect(Collectors.joining(SCOPE_SEPARATOR));

		// no need to check if reserved or note as it contains CLASS_FUNC_SEPARATOR
		return new Identifier.L(classPrefix + SCOPE_SEPARATOR + escape(methodName, IdentifierClass.LIDENT) + descriptor);
	}

	public Identifier.U escapeU(String input) {
		return new Identifier.U(escape(input, IdentifierClass.UIDENT));
	}

	/**
	 * Models the different identifier classes in WhyML. Identifier classes differ for which classes of characters are
	 * allowed in the first character of the identifiers
	 */
	public enum IdentifierClass {
		/**
		 * Models an LIDENT identifier according to the WhyML EBNF grammar. LIDENTs are used for type names, methods,
		 * and identifiers within a scope
		 */
		LIDENT('a', 'z', FORCE_LOWERCASE, '_'),

		/**
		 * Models a UIDENT identifier according to the WhyML EBND grammar, UIDENTs are used for scope and module names.
		 */
		UIDENT('A', 'Z', FORCE_UPPERCASE);

		private final char from;
		private final char to;
		private final String forceEscaper;
		private final char[] additional;

		IdentifierClass(char from, char to, String forceEscaper, char... additional) {
			this.from = from;
			this.to = to;
			this.forceEscaper = forceEscaper;
			this.additional = additional;
		}

		/**
		 * Returns true if the given character is a valid first character
		 *
		 * @param firstChar the first character in the identifier to validate
		 * @return true if valid, false if not
		 */
		public boolean validStart(int firstChar) {
			if (from <= firstChar && firstChar <= to) return true;
			for (char a : additional) {
				if (a == firstChar) return true;
			}
			return false;
		}

		public String getForceEscaper() {
			return forceEscaper;
		}
	}
}