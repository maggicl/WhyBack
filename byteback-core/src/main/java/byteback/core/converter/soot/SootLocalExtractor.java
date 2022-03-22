package byteback.core.converter.soot;

import byteback.core.representation.soot.body.SootExpressionVisitor;
import soot.Local;
import soot.Value;

public class SootLocalExtractor extends SootExpressionVisitor<Local> {

	private Local local;

	@Override
	public void caseLocal(final Local local) {
		this.local = local;
	}

	@Override
	public void caseDefault(final Value expression) {
		throw new IllegalArgumentException("Expected local definition, got " + expression);
	}

	@Override
	public Local result() {
		if (local == null) {
			throw new IllegalStateException("Could not retrieve local reference");
		} else {
			return local;
		}
	}

}
