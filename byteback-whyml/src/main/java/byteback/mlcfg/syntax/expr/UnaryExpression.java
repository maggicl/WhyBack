package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.prefix;
import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;

public final class UnaryExpression implements Expression {

	private final Operator operator;
	private final Expression operand;

	public UnaryExpression(Operator operator, Expression operand) {
		Expression.checkCompatibleType("1st", operand, operator.opType);

		this.operator = operator;
		this.operand = operand;
	}

	@Override
	public SExpr toWhy() {
		return prefix(operator.opName, operand.toWhy());
	}

	@Override
	public WhyJVMType type() {
		return this.operator.returnType;
	}

	public enum Operator {
		DNEG("dneg", WhyJVMType.DOUBLE, WhyJVMType.DOUBLE),
		FNEG("fneg", WhyJVMType.FLOAT, WhyJVMType.FLOAT),
		INEG("ineg", WhyJVMType.INT, WhyJVMType.INT),
		COND_EQ("cond_eq", WhyJVMType.INT, WhyJVMType.BOOL),
		COND_NE("cond_ne", WhyJVMType.INT, WhyJVMType.BOOL),
		COND_LT("cond_lt", WhyJVMType.INT, WhyJVMType.BOOL),
		COND_GT("cond_gt", WhyJVMType.INT, WhyJVMType.BOOL),
		COND_LE("cond_le", WhyJVMType.INT, WhyJVMType.BOOL),
		COND_GE("cond_ge", WhyJVMType.INT, WhyJVMType.BOOL),
		LNEG("lneg", WhyJVMType.LONG, WhyJVMType.LONG),

		// Logic operation
		NOT("not", WhyJVMType.BOOL, WhyJVMType.BOOL);

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
