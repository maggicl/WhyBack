package byteback.mlcfg.printer;

public enum WhyBinaryOp {
	AND("&&"),
	OR("||"),
	EQ("="),
	DIV("/"),
	MUL("*"),
	LT("<"),
	LTE("<="),
	GT(">"),
	GTE(">="),
	ADD("+"),
	SUB("-"),
	NEQ("<>"),
	IMPLIES("->");



//	EquivalenceOperation (byteback.frontend.boogie.ast)
//  AdditionOperation (byteback.frontend.boogie.ast)


//	ModuloOperation (byteback.frontend.boogie.ast)


	private final String op;

	WhyBinaryOp(String op) {
		this.op = op;
	}

	public String getOp() {
		return op;
	}
}
