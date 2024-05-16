package byteback.whyml.syntax.expr;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;
import java.util.Locale;

public record PrimitiveCastExpression(Expression inner, WhyJVMType targetType) implements Expression {
	public PrimitiveCastExpression {
		if (inner.type().isMeta()) {
			throw new IllegalArgumentException("inner expression of primitive cast operation must have primitive type");
		}

		if (targetType.isMeta()) {
			throw new IllegalArgumentException("target type of primitive cast operation must be primitive");
		}
	}

	private static String getCastTypeName(WhyJVMType type) {
		return type.getWhyAccessorScope().toLowerCase(Locale.ROOT);
	}

	@Override
	public SExpr toWhy() {
		final WhyJVMType sourceType = inner.type();
		if (sourceType.isWholeNumber() || targetType.isWholeNumber()) {
			return prefix("int2" + getCastTypeName(targetType),
					prefix(getCastTypeName(sourceType) + "2int",
							inner.toWhy())
			);
		} else {
			return prefix("%s2%s".formatted(getCastTypeName(sourceType), getCastTypeName(targetType)), inner.toWhy());
		}
	}

	@Override
	public WhyJVMType type() {
		return targetType;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformPrimitiveCastExpression(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitPrimitiveCastExpression(this);
	}
}
