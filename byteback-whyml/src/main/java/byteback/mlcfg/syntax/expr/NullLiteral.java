package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.terminal;
import byteback.mlcfg.syntax.types.WhyJVMType;

public final class NullLiteral implements Expression {
	public static final NullLiteral INSTANCE = new NullLiteral();

	private NullLiteral() {
	}

	@Override
	public SExpr toWhy() {
		return terminal("Ptr.null");
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.PTR;
	}
}
