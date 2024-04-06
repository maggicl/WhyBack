package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.prefix;
import byteback.mlcfg.syntax.expr.transformer.ExpressionTransformer;
import byteback.mlcfg.syntax.expr.transformer.ExpressionVisitor;
import byteback.mlcfg.syntax.types.WhyJVMType;

// FIXME: old references might possibly be forbidden in spec functions, consider passing parameter copy of old values to fun
public class OldReference implements Expression {
	public Expression getInner() {
		return inner;
	}

	private final Expression inner;

	public OldReference(Expression inner) {
		this.inner = inner;
	}

	@Override
	public SExpr toWhy() {
		return prefix("old", inner.toWhy());
	}

	@Override
	public WhyJVMType type() {
		return inner.type();
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformOldReference(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitOldReference(this);
	}
}
