package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyPrimitive;
import byteback.mlcfg.syntax.types.WhyReference;
import byteback.mlcfg.syntax.types.WhyType;

public final class UnaryExpression extends Expression {
	private final Operator operator;
	private final Expression operand;

	public UnaryExpression(Operator operator, Expression operand) {
		checkCompatibleType(operand, operator.opType);

		this.operator = operator;
		this.operand = operand;
	}

	private static void checkCompatibleType(Expression operand, WhyType type) {
		if (type == WhyReference.OBJECT && !(operand.type() instanceof WhyReference) ||
				type instanceof WhyPrimitive && (operand.type() != type)) {
			throw new IllegalStateException("operand does not have required type " + type.getWhyType());
		}
	}

	@Override
	public String toWhy() {
		return "(%s %s)".formatted(operator.opName, operand.toWhy());
	}

	@Override
	public WhyType type() {
		return this.operator.returnType;
	}

	public enum Operator {
		D2F("d2f", WhyPrimitive.DOUBLE, WhyPrimitive.FLOAT),
		D2I("d2i", WhyPrimitive.DOUBLE, WhyPrimitive.INT),
		D2L("d2l", WhyPrimitive.DOUBLE, WhyPrimitive.LONG),
		DNEG("dneg", WhyPrimitive.DOUBLE, WhyPrimitive.DOUBLE),
		F2D("f2d", WhyPrimitive.FLOAT, WhyPrimitive.DOUBLE),
		F2I("f2i", WhyPrimitive.FLOAT, WhyPrimitive.INT),
		F2L("f2l", WhyPrimitive.FLOAT, WhyPrimitive.LONG),
		FNEG("fneg", WhyPrimitive.FLOAT, WhyPrimitive.FLOAT),
		I2B("i2b", WhyPrimitive.INT, WhyPrimitive.BYTE),
		I2C("i2c", WhyPrimitive.INT, WhyPrimitive.CHAR),
		I2S("i2s", WhyPrimitive.INT, WhyPrimitive.SHORT),
		I2L("i2l", WhyPrimitive.INT, WhyPrimitive.LONG),
		I2F("i2f", WhyPrimitive.INT, WhyPrimitive.FLOAT),
		I2D("i2d", WhyPrimitive.INT, WhyPrimitive.DOUBLE),
		INEG("ineg", WhyPrimitive.INT, WhyPrimitive.INT),
		COND_EQ("cond_eq", WhyPrimitive.INT, WhyPrimitive.BOOL),
		COND_NE("cond_ne", WhyPrimitive.INT, WhyPrimitive.BOOL),
		COND_LT("cond_lt", WhyPrimitive.INT, WhyPrimitive.BOOL),
		COND_GT("cond_gt", WhyPrimitive.INT, WhyPrimitive.BOOL),
		COND_LE("cond_le", WhyPrimitive.INT, WhyPrimitive.BOOL),
		COND_GE("cond_ge", WhyPrimitive.INT, WhyPrimitive.BOOL),
		L2I("l2i", WhyPrimitive.LONG, WhyPrimitive.INT),
		L2F("l2f", WhyPrimitive.LONG, WhyPrimitive.FLOAT),
		L2D("l2d", WhyPrimitive.LONG, WhyPrimitive.DOUBLE),
		LNEG("lneg", WhyPrimitive.LONG, WhyPrimitive.LONG),
		Z2I("z2i", WhyPrimitive.BOOL, WhyPrimitive.INT),
		I2Z("i2z", WhyPrimitive.INT, WhyPrimitive.BOOL);

		private final String opName;
		private final WhyType opType;
		private final WhyPrimitive returnType;

		Operator(String opName, WhyType opType, WhyPrimitive returnType) {
			this.opName = opName;
			this.opType = opType;
			this.returnType = returnType;
		}
	}
}
