package byteback.mlcfg.syntax.expr.binary;

import byteback.mlcfg.syntax.types.WhyPrimitive;
import byteback.mlcfg.syntax.types.WhyType;
import java.util.Optional;

public enum LogicConnector implements BinaryOperator {
	IFF("<->"),
	IMPLIES("->"),
	AND("&&"),
	OR("||");

	private final String opName;

	LogicConnector(String opName) {
		this.opName = opName;
	}

	@Override
	public String opName() {
		return opName;
	}

	@Override
	public WhyType firstOpType() {
		return WhyPrimitive.BOOL;
	}

	@Override
	public WhyType secondOpType() {
		return WhyPrimitive.BOOL;
	}

	@Override
	public WhyPrimitive returnType() {
		return WhyPrimitive.BOOL;
	}

	@Override
	public boolean isInfix() {
		return true;
	}
}
