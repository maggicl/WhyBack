package byteback.whyml.syntax.expr.binary;

import byteback.whyml.syntax.type.WhyJVMType;

public interface BinaryOperator {
	String opName();

	default String logicalOpName() {
		return opName();
	}

	WhyJVMType firstOpType();

	WhyJVMType secondOpType();

	WhyJVMType returnType();

	boolean isInfix();
}
