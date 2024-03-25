package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyJVMType;
import com.google.common.base.Charsets;

public class StringLiteralExpression implements Expression {
	private final String str;

	public StringLiteralExpression(String str) {
		this.str = str;
	}

	private static String mapChar(byte c) {
		if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || c >= 'a' && c <= 'z') {
			return Character.toString(c);
		} else if (c == '\\') {
			return "\\\\";
		} else if (c == '"') {
			return "\\\"";
		} else if (c == '\n') {
			return "\\n";
		} else if (c == '\r') {
			return "\\r";
		} else if (c == '\t') {
			return "\\t";
		} else {
			return "\\x%02X".formatted((int) c & 0xff);
		}
	}

	@Override
	public String toWhy() {
		final StringBuilder sb = new StringBuilder("\"");
		for (final byte b : str.getBytes(Charsets.UTF_8)) {
			sb.append(mapChar(b));
		}
		sb.append("\"");

		final String literal = sb.toString();
		return "(java.lang.String.literal'8 heap %s)".formatted(literal);
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.PTR;
	}
}
