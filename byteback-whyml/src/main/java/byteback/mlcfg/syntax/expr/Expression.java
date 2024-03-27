package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import byteback.mlcfg.syntax.expr.transformer.ExpressionTransformer;
import byteback.mlcfg.syntax.expr.transformer.ExpressionVisitor;
import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;

public interface Expression {
	SExpr toWhy();

	WhyJVMType type();

	Expression accept(ExpressionTransformer transformer);

	void accept(ExpressionVisitor visitor);

	static void checkCompatibleType(String operandPos, Expression operand, WhyType type) {
		if (operand.type() != type.jvm()) {
			throw new IllegalStateException("%s operand does not have required type %s but %s".formatted(
					operandPos, operand.type(), type.jvm()));
		}
	}
}
