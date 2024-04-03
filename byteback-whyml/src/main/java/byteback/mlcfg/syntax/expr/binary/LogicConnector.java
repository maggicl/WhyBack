package byteback.mlcfg.syntax.expr.binary;

import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;

public enum LogicConnector implements BinaryOperator {
	IFF("<->."),
	IMPLIES("->."),
	AND("/\\."), // FIXME: check semantics for bitwise bool operations (non-short-circuited)
	OR("\\/.");

	private final String opName;

	LogicConnector(String opName) {
		this.opName = opName;
	}

	@Override
	public String opName() {
		return opName;
	}

	@Override
	public WhyJVMType firstOpType() {
		return WhyJVMType.BOOL;
	}

	@Override
	public WhyJVMType secondOpType() {
		return WhyJVMType.BOOL;
	}

	@Override
	public WhyJVMType returnType() {
		return WhyJVMType.BOOL;
	}

	@Override
	public boolean isInfix() {
		return true;
	}
}
