package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyJVMType;

public class OldReference implements Expression {
	private final Expression inner;

	public OldReference(Expression inner) {
		this.inner = inner;
	}

	@Override
	public String toWhy() {
		return "(old %s)".formatted(inner.toWhy());
	}

	@Override
	public WhyJVMType type() {
		return inner.type();
	}
}
