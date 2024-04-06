package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.infix;
import static byteback.mlcfg.printer.SExpr.prefix;
import static byteback.mlcfg.printer.SExpr.terminal;
import byteback.mlcfg.syntax.WhyFunctionParam;
import byteback.mlcfg.syntax.expr.transformer.ExpressionTransformer;
import byteback.mlcfg.syntax.expr.transformer.ExpressionVisitor;
import byteback.mlcfg.syntax.types.WhyJVMType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
