package byteback.vimp.internal;

import soot.Local;
import soot.Value;
import soot.jimple.Expr;
import soot.util.Chain;
import soot.util.HashChain;

public class QuantifierExpr implements LogicExpr {

	final Chain<Local> freeLocals;

	final Value value;

	public QuantifierExpr(final Value value) {
		this.freeLocals = new HashChain<>();
		this.value = value;
	}

	public String toString() {
		final var builder = new StringBuilder();
		builder.append("(");

		for (final Local local : freeLocals) {
			builder.append(local.getType().toString());
			builder.append(" ");
			builder.append(local.getName().toString());
		}

		builder.append(" :: ");
		builder.append(value.toString());
		builder.append(")");

		return builder.toString();
	}
	
}
