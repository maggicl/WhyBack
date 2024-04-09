package byteback.whyml.syntax.expr.binary;

import byteback.whyml.syntax.type.WhyJVMType;

public enum PrefixOperator implements BinaryOperator {
	DADD("dadd", WhyJVMType.DOUBLE, WhyJVMType.DOUBLE, WhyJVMType.DOUBLE),
	DSUB("dsub", WhyJVMType.DOUBLE, WhyJVMType.DOUBLE, WhyJVMType.DOUBLE),
	DMUL("dmul", WhyJVMType.DOUBLE, WhyJVMType.DOUBLE, WhyJVMType.DOUBLE),
	DDIV("ddiv", WhyJVMType.DOUBLE, WhyJVMType.DOUBLE, WhyJVMType.DOUBLE),
	DREM("drem", WhyJVMType.DOUBLE, WhyJVMType.DOUBLE, WhyJVMType.DOUBLE),
	DCMPL("dcmpl", WhyJVMType.DOUBLE, WhyJVMType.DOUBLE, WhyJVMType.INT),
	DCMPG("dcmpg", WhyJVMType.DOUBLE, WhyJVMType.DOUBLE, WhyJVMType.INT),
	FADD("fadd", WhyJVMType.FLOAT, WhyJVMType.FLOAT, WhyJVMType.FLOAT),
	FSUB("fsub", WhyJVMType.FLOAT, WhyJVMType.FLOAT, WhyJVMType.FLOAT),
	FMUL("fmul", WhyJVMType.FLOAT, WhyJVMType.FLOAT, WhyJVMType.FLOAT),
	FDIV("fdiv", WhyJVMType.FLOAT, WhyJVMType.FLOAT, WhyJVMType.FLOAT),
	FREM("frem", WhyJVMType.FLOAT, WhyJVMType.FLOAT, WhyJVMType.FLOAT),
	FCMPL("fcmpl", WhyJVMType.FLOAT, WhyJVMType.FLOAT, WhyJVMType.INT),
	FCMPG("fcmpg", WhyJVMType.FLOAT, WhyJVMType.FLOAT, WhyJVMType.INT),
	IADD("iadd", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.INT),
	ISUB("isub", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.INT),
	IMUL("imul", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.INT),
	IDIV("idiv", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.INT),
	IREM("irem", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.INT),
	IAND("iand", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.INT),
	IOR("ior", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.INT),
	IXOR("ixor", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.INT),
	ISHL("ishl", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.INT),
	ISHR("ishr", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.INT),
	IUSHR("iushr", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.INT),
	COND_ICMPEQ("cond_icmpeq", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.BOOL),
	COND_ICMPNE("cond_icmpne", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.BOOL),
	COND_ICMPLT("cond_icmplt", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.BOOL),
	COND_ICMPGT("cond_icmpgt", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.BOOL),
	COND_ICMPLE("cond_icmple", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.BOOL),
	COND_ICMPGE("cond_icmpge", WhyJVMType.INT, WhyJVMType.INT, WhyJVMType.BOOL),
	LADD("ladd", WhyJVMType.LONG, WhyJVMType.LONG, WhyJVMType.LONG),
	LSUB("lsub", WhyJVMType.LONG, WhyJVMType.LONG, WhyJVMType.LONG),
	LMUL("lmul", WhyJVMType.LONG, WhyJVMType.LONG, WhyJVMType.LONG),
	LDIV("ldiv", WhyJVMType.LONG, WhyJVMType.LONG, WhyJVMType.LONG),
	LREM("lrem", WhyJVMType.LONG, WhyJVMType.LONG, WhyJVMType.LONG),
	LAND("land", WhyJVMType.LONG, WhyJVMType.LONG, WhyJVMType.LONG),
	LOR("lor", WhyJVMType.LONG, WhyJVMType.LONG, WhyJVMType.LONG),
	LXOR("lxor", WhyJVMType.LONG, WhyJVMType.LONG, WhyJVMType.LONG),
	LSHL("lshl", WhyJVMType.LONG, WhyJVMType.INT, WhyJVMType.LONG),
	LSHR("lshr", WhyJVMType.LONG, WhyJVMType.INT, WhyJVMType.LONG),
	LUSHR("lushr", WhyJVMType.LONG, WhyJVMType.LONG, WhyJVMType.LONG),
	LCMP("lcmp", WhyJVMType.LONG, WhyJVMType.LONG, WhyJVMType.INT),
	COND_ACMPEQ("cond_acmpeq", WhyJVMType.PTR, WhyJVMType.PTR, WhyJVMType.INT),
	COND_ACMPNE("cond_acmpne", WhyJVMType.PTR, WhyJVMType.PTR, WhyJVMType.INT);

	private final String opName;
	private final WhyJVMType firstOpType;
	private final WhyJVMType secondOpType;
	private final WhyJVMType returnType;

	PrefixOperator(String opName, WhyJVMType firstOpType, WhyJVMType secondOpType, WhyJVMType returnType) {
		this.opName = opName;
		this.firstOpType = firstOpType;
		this.secondOpType = secondOpType;
		this.returnType = returnType;
	}

	@Override
	public String opName() {
		return opName;
	}

	@Override
	public WhyJVMType firstOpType() {
		return firstOpType;
	}

	@Override
	public WhyJVMType secondOpType() {
		return secondOpType;
	}

	@Override
	public WhyJVMType returnType() {
		return returnType;
	}

	@Override
	public boolean isInfix() {
		return false;
	}
}
