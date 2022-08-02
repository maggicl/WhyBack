package byteback.core.representation.soot.type;

import byteback.core.representation.Visitor;
import soot.Type;
import soot.TypeSwitch;

/**
 * Base class for a {@link SootType} visitor.
 */
public abstract class SootTypeVisitor<R> extends TypeSwitch<R> implements Visitor<Type, R> {

	@Override
	public void caseDefault(Type type) {
	}

	@Override
	public void defaultCase(Type type) {
		caseDefault(type);
	}

	public R visit(Type type) {
		type.apply(this);

		return result();
	}

}
