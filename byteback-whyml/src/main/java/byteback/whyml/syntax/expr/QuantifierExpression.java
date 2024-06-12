package byteback.whyml.syntax.expr;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.infix;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.syntax.type.WhyJVMType;

public record QuantifierExpression(QuantifierExpression.Kind kind,
								   WhyLocal variable,
								   Expression inner) implements Expression {
	@Override
	public SExpr toWhy(boolean useLogicOps) {
		final SExpr innerBody = inner.toWhy(useLogicOps);

		final SExpr body = variable.condition().map(expression -> expression.toWhy(useLogicOps))
				.map(sExpr -> infix("->", sExpr, innerBody))
				.orElse(innerBody);

		if (useLogicOps) {
			return prefix(
					kind.logicalSymbol,
					terminal("%s: %s.".formatted(variable.name(), variable.type().getWhyType())),
					body
			);
		} else {
			return prefix(
					kind.symbol,
					terminal(Identifier.Special.HEAP),
					prefix(
							"fun",
							terminal("(%s: Heap.t)".formatted(Identifier.Special.HEAP)),
							terminal("(%s: %s)".formatted(variable.name(), variable.type().getWhyType())),
							terminal("->"),
							body
					)
			);
		}
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.BOOL;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformQuantifierExpression(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitQuantifierExpression(this);
	}

	public enum Kind {
		EXISTS("q_exists", "exists"),
		FORALL("q_forall", "forall");

		private final String symbol;
		private final String logicalSymbol;

		Kind(String symbol, String logicalSymbol) {
			this.symbol = symbol;
			this.logicalSymbol = logicalSymbol;
		}
	}
}
