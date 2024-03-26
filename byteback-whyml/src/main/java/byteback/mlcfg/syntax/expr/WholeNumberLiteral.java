package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.terminal;
import byteback.mlcfg.syntax.types.WhyJVMType;

public final class WholeNumberLiteral implements Expression {

	private final WhyJVMType type;
	private final long value;

	public WholeNumberLiteral(WhyJVMType type, long value) {
		if (!type.isWholeNumber()) {
			throw new IllegalArgumentException("literal has not valid numeric type: " + type);
		}

		this.type = type;
		this.value = value;
	}

	@Override
	public SExpr toWhy() {
		return terminal("(%d:%s)".formatted(value, type.getWhyType()));
	}

	@Override
	public WhyJVMType type() {
		return type;
	}
}
