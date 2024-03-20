package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyReference;
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
	public String toWhy() {
		return "(%s %s)".formatted(operator.opName, operand.toWhy());
	}

	@Override
	public WhyJVMType type() {
		return this.operator.returnType;
	}

	public enum Operator {
		D2F("d2f", WhyJVMType.DOUBLE, WhyJVMType.FLOAT),
		D2I("d2i", WhyJVMType.DOUBLE, WhyJVMType.INT),
		D2L("d2l", WhyJVMType.DOUBLE, WhyJVMType.LONG),
		DNEG("dneg", WhyJVMType.DOUBLE, WhyJVMType.DOUBLE),
		F2D("f2d", WhyJVMType.FLOAT, WhyJVMType.DOUBLE),
		F2I("f2i", WhyJVMType.FLOAT, WhyJVMType.INT),
		F2L("f2l", WhyJVMType.FLOAT, WhyJVMType.LONG),
		FNEG("fneg", WhyJVMType.FLOAT, WhyJVMType.FLOAT),
		I2B("i2b", WhyJVMType.INT, WhyJVMType.BYTE),
		I2C("i2c", WhyJVMType.INT, WhyJVMType.CHAR),
		I2S("i2s", WhyJVMType.INT, WhyJVMType.SHORT),
		I2L("i2l", WhyJVMType.INT, WhyJVMType.LONG),
		I2F("i2f", WhyJVMType.INT, WhyJVMType.FLOAT),
		I2D("i2d", WhyJVMType.INT, WhyJVMType.DOUBLE),
		INEG("ineg", WhyJVMType.INT, WhyJVMType.INT),
		COND_EQ("cond_eq", WhyJVMType.INT, WhyJVMType.BOOL),
		COND_NE("cond_ne", WhyJVMType.INT, WhyJVMType.BOOL),
		COND_LT("cond_lt", WhyJVMType.INT, WhyJVMType.BOOL),
		COND_GT("cond_gt", WhyJVMType.INT, WhyJVMType.BOOL),
		COND_LE("cond_le", WhyJVMType.INT, WhyJVMType.BOOL),
		COND_GE("cond_ge", WhyJVMType.INT, WhyJVMType.BOOL),
		L2I("l2i", WhyJVMType.LONG, WhyJVMType.INT),
		L2F("l2f", WhyJVMType.LONG, WhyJVMType.FLOAT),
		L2D("l2d", WhyJVMType.LONG, WhyJVMType.DOUBLE),
		LNEG("lneg", WhyJVMType.LONG, WhyJVMType.LONG),
		Z2I("z2i", WhyJVMType.BOOL, WhyJVMType.INT),
		I2Z("i2z", WhyJVMType.INT, WhyJVMType.BOOL),

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
