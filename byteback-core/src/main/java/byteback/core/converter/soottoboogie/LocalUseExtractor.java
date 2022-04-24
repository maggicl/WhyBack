package byteback.core.converter.soottoboogie;

import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import soot.Local;
import soot.ValueBox;

/**
 * Extractor class for the {@link Local} subexpressions present in a given
 * expression.
 *
 * @author paganma
 */
public class LocalUseExtractor extends SootExpressionVisitor<Set<Local>> {

	private final Set<Local> usedLocals;

	public LocalUseExtractor() {
		this.usedLocals = new HashSet<>();
	}

	/**
	 * Extracts all the locals used in a given expression.
	 *
	 * @param expression
	 *            The expression to be scanned.
	 * @return The {@link Local} subexpressions present in the given expression.
	 */
	public Set<Local> visit(final SootExpression expression) {
		final Collection<ValueBox> useBoxes = expression.getUseBoxes();

		for (ValueBox useBox : useBoxes) {
			useBox.getValue().apply(this);
		}

		return usedLocals;
	}

	@Override
	public void caseLocal(final Local local) {
		usedLocals.add(local);
	}

}
