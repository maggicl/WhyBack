package byteback.whyml.syntax.expr;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.infix;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.function.WhyFunctionParam;
import byteback.whyml.syntax.type.WhyJVMType;

public record QuantifierExpression(QuantifierExpression.Kind kind,
								   WhyFunctionParam variable,
								   Expression inner) implements Expression {
	@Override
	public SExpr toWhy() {
		final SExpr body = variable.condition().map(Expression::toWhy)
				.map(sExpr -> infix("->", sExpr, inner.toWhy()))
				.orElseGet(inner::toWhy);

		return prefix(
				"q_%s".formatted(kind.symbol),
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
		EXISTS("exists"),
		FORALL("forall");

		private final String symbol;

		Kind(String symbol) {
			this.symbol = symbol;
		}
	}
}
