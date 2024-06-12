package byteback.whyml.syntax.expr.binary;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.infix;
import static byteback.whyml.printer.SExpr.prefix;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;

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

	public BinaryOperator getOperator() {
		return operator;
	}

	public Expression getFirstOperand() {
		return firstOperand;
	}

	public Expression getSecondOperand() {
		return secondOperand;
	}

	@Override
	public SExpr toWhy(boolean useLogicOps) {
		final String opName = useLogicOps ? operator.logicalOpName() : operator.opName();
		final SExpr firstOp = firstOperand.toWhy(useLogicOps);
		final SExpr secondOp = secondOperand.toWhy(useLogicOps);

		return operator.isInfix()
				? infix(opName, firstOp, secondOp)
				: prefix(opName, firstOp, secondOp);
	}

	@Override
	public WhyJVMType type() {
		return this.operator.returnType();
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformBinaryExpression(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitBinaryExpression(this);
	}
}
