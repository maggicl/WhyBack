package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.prefix;
import byteback.mlcfg.syntax.types.WhyJVMType;

public class OldReference implements Expression {
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
}
