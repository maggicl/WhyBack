package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.prefix;
import static byteback.mlcfg.printer.SExpr.terminal;
import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;

public class InstanceOfExpression implements Expression {
	private final Expression reference;
	private final WhyType type;

	public InstanceOfExpression(Expression reference, WhyType type) {
		if (reference.type() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("instanceof expression must have type PTR, given " + reference.type());
		}

		if (type.jvm() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("instanceof type to check must have JVM type PTR, given " + type.jvm());
		}

		this.reference = reference;
		this.type = type;
	}

	@Override
	public SExpr toWhy() {
		return prefix(
				"instanceof",
				terminal("heap"),
				reference.toWhy(),
				terminal(type.getPreludeType())
		);
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.PTR;
	}
}
