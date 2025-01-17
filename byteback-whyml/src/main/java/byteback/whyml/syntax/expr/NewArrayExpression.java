package byteback.whyml.syntax.expr;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;
import java.util.Locale;

public record NewArrayExpression(WhyType baseType, Expression size) implements Expression {
	@Override
	public WhyJVMType type() {
		return WhyJVMType.PTR;
	}

	@Override
	public SExpr toWhy(boolean useLogicOps) {
		if (baseType instanceof WhyJVMType type) {
			if (type.isMeta()) {
				throw new IllegalArgumentException("An array base JVM type cannot be meta: " + type);
			}

			return prefix(
					"%snewarray".formatted(type.getWhyAccessorScope().toLowerCase(Locale.ROOT)),
					terminal(Identifier.Special.HEAP),
					size.toWhy(useLogicOps)
			);
		} else {
			return prefix(
					"lnewarray",
					terminal(Identifier.Special.HEAP),
					baseType.getPreludeType(),
					size.toWhy(useLogicOps)
			);
		}
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformNewArrayExpression(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitNewArrayExpression(this);
	}
}
