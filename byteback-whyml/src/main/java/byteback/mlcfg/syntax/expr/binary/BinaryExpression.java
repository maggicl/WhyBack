package byteback.mlcfg.syntax.expr.binary;

import byteback.mlcfg.syntax.expr.Expression;
import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;

public final class BinaryExpression implements Expression {
	private final BinaryOperator operator;
	private final Expression firstOperand;
	private final Expression secondOperand;

	public BinaryExpression(BinaryOperator operator, Expression firstOperand, Expression secondOperand) {
		Expression.checkCompatibleType("1st", firstOperand, operator.firstOpType());
		Expression.checkCompatibleType("2nd", secondOperand, operator.secondOpType());

		this.operator = operator;
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
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
	public WhyJVMType type() {
		return this.operator.returnType();
	}
}
