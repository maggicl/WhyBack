package byteback.whyml.syntax.expr;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyReference;
import com.google.common.base.Charsets;

public record ClassLiteralExpression(WhyReference classType) implements Expression {
	@Override
	public SExpr toWhy() {
		return prefix(
				"%s.literal%s".formatted(Identifier.Special.CLASS, IdentifierEscaper.PRELUDE_RESERVED),
				terminal("%s.class".formatted(classType.fqdn()))
		);
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.PTR;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformClassLiteralExpression(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitClassLiteralExpression(this);
	}
}
