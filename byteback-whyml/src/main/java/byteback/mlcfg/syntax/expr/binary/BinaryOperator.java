package byteback.mlcfg.syntax.expr.binary;

import byteback.mlcfg.syntax.types.WhyPrimitive;
import byteback.mlcfg.syntax.types.WhyType;

public interface BinaryOperator {
	String opName();

	WhyType firstOpType();

	WhyType secondOpType();

	WhyPrimitive returnType();

	boolean isInfix();
}
