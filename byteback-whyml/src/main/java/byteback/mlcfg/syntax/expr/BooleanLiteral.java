package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.terminal;
import byteback.mlcfg.syntax.expr.transformer.ExpressionTransformer;
import byteback.mlcfg.syntax.expr.transformer.ExpressionVisitor;
import byteback.mlcfg.syntax.types.WhyJVMType;

public final class BooleanLiteral implements Expression {
	private final boolean value;

	public BooleanLiteral(boolean value) {
		this.value = value;
	}

	@Override
	public SExpr toWhy() {
		return terminal(value ? "true" : "false");
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.BOOL;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformBooleanLiteral(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitBooleanLiteral(this);
	}
}
