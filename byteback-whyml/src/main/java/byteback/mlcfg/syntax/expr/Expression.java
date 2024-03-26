package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;

public interface Expression {
	SExpr toWhy();

	WhyJVMType type();

	static void checkCompatibleType(String operandPos, Expression operand, WhyType type) {
		if (operand.type() != type.jvm()) {
			throw new IllegalStateException("%s operand does not have required type %s but %s".formatted(
					operandPos, operand.type(), type.jvm()));
		}
	}
}
