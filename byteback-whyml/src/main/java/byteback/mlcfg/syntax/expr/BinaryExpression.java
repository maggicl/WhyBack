package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyPrimitive;
import byteback.mlcfg.syntax.types.WhyReference;
import byteback.mlcfg.syntax.types.WhyType;

public final class BinaryExpression extends Expression {
	private final Operator operator;
	private final Expression firstOperand;
	private final Expression secondOperand;

	public BinaryExpression(Operator operator, Expression firstOperand, Expression secondOperand) {
		checkCompatibleType(firstOperand, operator.firstOpType, "1st");
		checkCompatibleType(secondOperand, operator.secondOpType, "2nd");

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
		return "(%s %s %s)".formatted(operator.opName, firstOperand.toWhy(), secondOperand.toWhy());
	}

	@Override
	public WhyType type() {
		return this.operator.returnType;
	}

	public enum Operator {
		DADD("dadd", WhyPrimitive.DOUBLE, WhyPrimitive.DOUBLE, WhyPrimitive.DOUBLE),
		DSUB("dsub", WhyPrimitive.DOUBLE, WhyPrimitive.DOUBLE, WhyPrimitive.DOUBLE),
		DMUL("dmul", WhyPrimitive.DOUBLE, WhyPrimitive.DOUBLE, WhyPrimitive.DOUBLE),
		DDIV("ddiv", WhyPrimitive.DOUBLE, WhyPrimitive.DOUBLE, WhyPrimitive.DOUBLE),
		DREM("drem", WhyPrimitive.DOUBLE, WhyPrimitive.DOUBLE, WhyPrimitive.DOUBLE),
		COND_DCMPL("cond_dcmpl", WhyPrimitive.DOUBLE, WhyPrimitive.DOUBLE, WhyPrimitive.INT),
		COND_DCMPG("cond_dcmpg", WhyPrimitive.DOUBLE, WhyPrimitive.DOUBLE, WhyPrimitive.INT),
		FADD("fadd", WhyPrimitive.FLOAT, WhyPrimitive.FLOAT, WhyPrimitive.FLOAT),
		FSUB("fsub", WhyPrimitive.FLOAT, WhyPrimitive.FLOAT, WhyPrimitive.FLOAT),
		FMUL("fmul", WhyPrimitive.FLOAT, WhyPrimitive.FLOAT, WhyPrimitive.FLOAT),
		FDIV("fdiv", WhyPrimitive.FLOAT, WhyPrimitive.FLOAT, WhyPrimitive.FLOAT),
		FREM("frem", WhyPrimitive.FLOAT, WhyPrimitive.FLOAT, WhyPrimitive.FLOAT),
		COND_FCMPL("cond_fcmpl", WhyPrimitive.FLOAT, WhyPrimitive.FLOAT, WhyPrimitive.INT),
		COND_FCMPG("cond_fcmpg", WhyPrimitive.FLOAT, WhyPrimitive.FLOAT, WhyPrimitive.INT),
		IADD("iadd", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.INT),
		ISUB("isub", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.INT),
		IMUL("imul", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.INT),
		IDIV("idiv", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.INT),
		IREM("irem", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.INT),
		IAND("iand", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.INT),
		IOR("ior", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.INT),
		IXOR("ixor", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.INT),
		ISHL("ishl", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.INT),
		ISHR("ishr", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.INT),
		IUSHR("iushr", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.INT),
		COND_ICMPEQ("cond_icmpeq", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.BOOL),
		COND_ICMPNE("cond_icmpne", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.BOOL),
		COND_ICMPLT("cond_icmplt", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.BOOL),
		COND_ICMPGT("cond_icmpgt", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.BOOL),
		COND_ICMPLE("cond_icmple", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.BOOL),
		COND_ICMPGE("cond_icmpge", WhyPrimitive.INT, WhyPrimitive.INT, WhyPrimitive.BOOL),
		LADD("ladd", WhyPrimitive.LONG, WhyPrimitive.LONG, WhyPrimitive.LONG),
		LSUB("lsub", WhyPrimitive.LONG, WhyPrimitive.LONG, WhyPrimitive.LONG),
		LMUL("lmul", WhyPrimitive.LONG, WhyPrimitive.LONG, WhyPrimitive.LONG),
		LDIV("ldiv", WhyPrimitive.LONG, WhyPrimitive.LONG, WhyPrimitive.LONG),
		LREM("lrem", WhyPrimitive.LONG, WhyPrimitive.LONG, WhyPrimitive.LONG),
		LAND("land", WhyPrimitive.LONG, WhyPrimitive.LONG, WhyPrimitive.LONG),
		LOR("lor", WhyPrimitive.LONG, WhyPrimitive.LONG, WhyPrimitive.LONG),
		LXOR("lxor", WhyPrimitive.LONG, WhyPrimitive.LONG, WhyPrimitive.LONG),
		LSHL("lshl", WhyPrimitive.LONG, WhyPrimitive.INT, WhyPrimitive.LONG),
		LSHR("lshr", WhyPrimitive.LONG, WhyPrimitive.INT, WhyPrimitive.LONG),
		LUSHR("lushr", WhyPrimitive.LONG, WhyPrimitive.LONG, WhyPrimitive.LONG),
		COND_LCMP("cond_lcmp", WhyPrimitive.LONG, WhyPrimitive.LONG, WhyPrimitive.INT),
		COND_ACMPEQ("cond_acmpeq", WhyReference.OBJECT, WhyReference.OBJECT, WhyPrimitive.INT),
		COND_ACMPNE("cond_acmpne", WhyReference.OBJECT, WhyReference.OBJECT, WhyPrimitive.INT);

		private final String opName;
		private final WhyType firstOpType;
		private final WhyType secondOpType;
		private final WhyPrimitive returnType;

		Operator(String opName, WhyType firstOpType, WhyType secondOpType, WhyPrimitive returnType) {
			this.opName = opName;
			this.firstOpType = firstOpType;
			this.secondOpType = secondOpType;
			this.returnType = returnType;
		}
	}
}
