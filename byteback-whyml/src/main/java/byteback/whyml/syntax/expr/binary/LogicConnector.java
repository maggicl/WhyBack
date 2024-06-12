package byteback.whyml.syntax.expr.binary;

import byteback.whyml.syntax.type.WhyJVMType;

public enum LogicConnector implements BinaryOperator {
	IFF("<->.", "<->"),
	IMPLIES("->.", "->"),
	AND("/\\.", "/\\"), // FIXME: check semantics for bitwise bool operations (non-short-circuited)
	OR("\\/.", "\\/");

	private final String opName;
	private final String logicalOpName;

	LogicConnector(String opName, String logicalOpName) {
		this.opName = opName;
		this.logicalOpName = logicalOpName;
	}

	@Override
	public String logicalOpName() {
		return logicalOpName;
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
