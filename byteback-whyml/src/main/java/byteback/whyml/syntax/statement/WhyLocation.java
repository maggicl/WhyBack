package byteback.whyml.syntax.statement;

public record WhyLocation(String file, int lineNumber, int startChar, int endChar) {
	public String toWhy() {
		int line = Math.max(0, lineNumber);
		return "[# \"%s\" %d %d %d %d ]".formatted(file, line, Math.max(0, startChar), line, Math.max(0, endChar));
	}
}
