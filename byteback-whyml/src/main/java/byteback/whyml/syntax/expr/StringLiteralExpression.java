package byteback.whyml.syntax.expr;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;
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
	public SExpr toWhy(boolean useLogicOps) {
		final StringBuilder sb = new StringBuilder("\"");
		for (final byte b : str.getBytes(Charsets.UTF_8)) {
			sb.append(mapChar(b));
		}
		sb.append("\"");

		return prefix(
				"%s.literal".formatted(Identifier.Special.STRING),
				terminal(sb.toString())
		);
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.PTR;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformStringLiteralExpression(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitStringLiteralExpression(this);
	}
}
