package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;

public class ClassCastExpression implements Expression {
	private final Expression reference;
	private final WhyType type;

	public ClassCastExpression(Expression reference, WhyType type) {
		if (reference.type() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("checkcast expression must have type PTR, given " + reference.type());
		}

		if (type.jvm() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("checkcast type to check must have JVM type PTR, given " + type.jvm());
		}

		this.reference = reference;
		this.type = type;
	}

	@Override
	public String toWhy() {
		return "(checkcast heap (%s) (%s))".formatted(
				reference.toWhy(),
				type.getPreludeType()
		);
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.BOOL;
	}
}
