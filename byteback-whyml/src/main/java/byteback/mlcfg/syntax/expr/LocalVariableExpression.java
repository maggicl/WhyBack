package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.types.WhyJVMType;

public class LocalVariableExpression implements Expression {
	private final Identifier.L name;
	private final WhyJVMType type;

	public LocalVariableExpression(Identifier.L name, WhyJVMType type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String toWhy() {
		return name.toString();
	}

	@Override
	public WhyJVMType type() {
		return type;
	}
}
