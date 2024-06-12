package byteback.whyml.syntax.expr;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;

public record UnaryExpression(UnaryExpression.Operator operator,
							  Expression operand) implements Expression {

	public UnaryExpression {
		Expression.checkCompatibleType("1st", operand, operator.opType);
	}

	@Override
	public SExpr toWhy(boolean useLogicOps) {
		return prefix(operator.opName, operand.toWhy(useLogicOps));
	}

	@Override
	public WhyJVMType type() {
		return this.operator.returnType;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformUnaryExpression(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitUnaryExpression(this);
	}

	public enum Operator {
		DNEG("dneg", WhyJVMType.DOUBLE, WhyJVMType.DOUBLE),
		FNEG("fneg", WhyJVMType.FLOAT, WhyJVMType.FLOAT),
		INEG("ineg", WhyJVMType.INT, WhyJVMType.INT),
		LNEG("lneg", WhyJVMType.LONG, WhyJVMType.LONG),

		// Logic operation
		NOT("not", WhyJVMType.BOOL, WhyJVMType.BOOL),

		/**
		 * Used exclusively for asserting that the parameter matching 'this' is not null
		 */
		NOT_NULL("not_null", WhyJVMType.PTR, WhyJVMType.BOOL);

		private final String opName;
		private final WhyType opType;
		private final WhyJVMType returnType;

		Operator(String opName, WhyType opType, WhyJVMType returnType) {
			this.opName = opName;
			this.opType = opType;
			this.returnType = returnType;
		}
	}
}
