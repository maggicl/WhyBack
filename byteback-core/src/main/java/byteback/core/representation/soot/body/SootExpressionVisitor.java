package byteback.core.representation.soot.body;

import byteback.core.representation.Visitor;
import soot.Value;
import soot.grimp.AbstractGrimpValueSwitch;

public abstract class SootExpressionVisitor<R> extends AbstractGrimpValueSwitch<R> implements Visitor<Value, R> {

	public void defaultCase(final Object object) {
		caseDefault((Value) object);
	}

	public R visit(final Value value) {
		value.apply(this);

		return result();
	}

}
