package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyJVMType;

public final class NullLiteral implements Expression {
	public static final NullLiteral INSTANCE = new NullLiteral();

	private NullLiteral() {
	}

	@Override
	public String toWhy() {
		return "Ptr.null";
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.PTR;
	}
}
