package byteback.mlcfg.syntax.expr.binary;

import byteback.mlcfg.syntax.expr.Expression;
import byteback.mlcfg.syntax.types.WhyPrimitive;
import byteback.mlcfg.syntax.types.WhyReference;
import byteback.mlcfg.syntax.types.WhyType;

public final class BinaryExpression implements Expression {
	private final BinaryOperator operator;
	private final Expression firstOperand;
	private final Expression secondOperand;

	public BinaryExpression(BinaryOperator operator, Expression firstOperand, Expression secondOperand) {
		checkCompatibleType(firstOperand, operator.firstOpType(), "1st");
		checkCompatibleType(secondOperand, operator.secondOpType(), "2nd");

		this.operator = operator;
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
	}

	private static void checkCompatibleType(Expression operand, WhyType type, String operandPos) {
		if (type == WhyReference.OBJECT && !(operand.type() instanceof WhyReference) ||
				type instanceof WhyPrimitive && (operand.type() != type)) {
			throw new IllegalStateException(operandPos + " operand does not have required type " + type.getWhyType());
		}
	}

	@Override
	public String toWhy() {
		if (operator.isInfix()) {
			return "(%s %s %s)".formatted(firstOperand.toWhy(), operator.opName(), secondOperand.toWhy());
		} else {
			return "(%s %s %s)".formatted(operator.opName(), firstOperand.toWhy(), secondOperand.toWhy());
		}
	}

	@Override
	public WhyType type() {
		return this.operator.returnType();
	}

}
