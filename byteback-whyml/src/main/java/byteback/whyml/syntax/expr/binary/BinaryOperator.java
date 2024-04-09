package byteback.whyml.syntax.expr.binary;

import byteback.whyml.syntax.type.WhyJVMType;

public interface BinaryOperator {
	String opName();

	WhyJVMType firstOpType();

	WhyJVMType secondOpType();

	WhyJVMType returnType();

	boolean isInfix();
}
