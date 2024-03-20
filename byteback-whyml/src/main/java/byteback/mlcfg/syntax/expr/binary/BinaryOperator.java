package byteback.mlcfg.syntax.expr.binary;

import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;

public interface BinaryOperator {
	String opName();

	WhyType firstOpType();

	WhyType secondOpType();

	WhyJVMType returnType();

	boolean isInfix();
}
