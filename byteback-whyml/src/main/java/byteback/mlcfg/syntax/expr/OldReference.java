package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.prefix;
import byteback.mlcfg.syntax.expr.transformer.ExpressionTransformer;
import byteback.mlcfg.syntax.types.WhyJVMType;

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
	public Expression visit(ExpressionTransformer transformer) {
		return transformer.transformOldReference(this);
	}
}
