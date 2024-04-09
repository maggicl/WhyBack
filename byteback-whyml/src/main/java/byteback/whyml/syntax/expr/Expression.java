package byteback.whyml.syntax.expr;

import byteback.whyml.printer.SExpr;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;

public interface Expression {
	SExpr toWhy();

	WhyJVMType type();

	default Expression asType(WhyJVMType type) {
		if (type == type()) return this;
		else throw new UnsupportedOperationException("type reinterpretation not supported");
	}

	Expression accept(ExpressionTransformer transformer);

	void accept(ExpressionVisitor visitor);

	static void checkCompatibleType(String operandPos, Expression operand, WhyType type) {
		if (operand.type() != type.jvm()) {
			throw new IllegalStateException("%s operand does not have required type %s but %s".formatted(
					operandPos, operand.type(), type.jvm()));
		}
	}
}
