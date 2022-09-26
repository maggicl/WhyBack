package byteback.vimp.internal;

import java.util.Iterator;
import java.util.List;

import soot.Local;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.util.Chain;
import soot.util.HashChain;

public abstract class QuantifierExpr implements LogicExpr {

	private Chain<Local> freeLocals;

	private Value value;

	public QuantifierExpr(final Value value, final Chain<Local> freeLocals) {
		this.freeLocals = freeLocals;
		this.value = value;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(final Value value) {
		this.value = value;
	}

	public Chain<Local> getFreeLocals() {
		return freeLocals;
	}

	public void setFreeLocals(final Chain<Local> freeLocals) {
		this.freeLocals = freeLocals;
	}

	public Chain<Local> cloneFreeLocals() {
		final Chain<Local> freeLocals =  new HashChain<>();

		for (Local local : getFreeLocals()) {
			freeLocals.add((Local) local.clone());
		}

		return freeLocals;
	}

	public abstract String getSymbol();

	@Override
	public void toString(final UnitPrinter up) {
		final Iterator<Local> freeIt = freeLocals.iterator();
		up.literal("(");
		up.literal(getSymbol());

		while (freeIt.hasNext()) {
			final Local local = freeIt.next();
			up.type(local.getType());
			local.toString(up);

			if (freeIt.hasNext()) {
				up.literal(", ");
			}
		}

		up.literal(" :: ");
		value.toString(up);
		up.literal(")");
	}

	@Override
	public List<ValueBox> getUseBoxes() {
		return value.getUseBoxes();
	}

	@Override
	public boolean equivTo(final Object o) {
		if (o instanceof QuantifierExpr q) {
			return q.value.equals(q.value);
		}

		return true;
	}

	@Override
	public int equivHashCode() {
		int hashCode = 17 ^ getSymbol().hashCode();

		for (Local local : freeLocals) {
			hashCode += local.equivHashCode();
		}

		return hashCode + (value.equivHashCode() * 101);
	}

  @Override
  public abstract Object clone();

}
