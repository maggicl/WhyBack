package byteback.whyml.syntax.expr;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.infix;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.function.WhyFunctionParam;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

public class QuantifierExpression implements Expression {
	private final Kind kind;
	private final WhyFunctionParam variable;
	private final Expression inner;

	public QuantifierExpression(Kind kind, WhyFunctionParam variable, Expression inner) {
		this.kind = kind;
		this.variable = variable;
		this.inner = inner;
	}

	public Kind getKind() {
		return kind;
	}

	public WhyFunctionParam getVariable() {
		return variable;
	}

	public Expression getInner() {
		return inner;
	}

	@Override
	public SExpr toWhy() {
		final SExpr body = variable.condition().map(SExpr::terminal)
				.map(sExpr -> infix("->", sExpr, inner.toWhy()))
				.orElseGet(inner::toWhy);

		return prefix(
				"q_%s".formatted(kind.symbol),
				prefix(
						"fun",
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
