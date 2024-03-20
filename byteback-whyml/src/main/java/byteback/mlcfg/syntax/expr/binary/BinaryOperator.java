package byteback.mlcfg.syntax.expr.binary;

import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;

public interface BinaryOperator {
	String opName();

	WhyJVMType firstOpType();

	WhyJVMType secondOpType();

	WhyJVMType returnType();

	boolean isInfix();
}
