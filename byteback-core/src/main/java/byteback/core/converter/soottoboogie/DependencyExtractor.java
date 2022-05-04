package byteback.core.converter.soottoboogie;

import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import soot.Local;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.toolkits.infoflow.CachedEquivalentValue;

/**
 * Extracts all the references upon which a given expression may depend.
 *
 * @author paganma
 */
public class DependencyExtractor extends SootExpressionVisitor<Set<Local>> {

	private final Set<Value> dependencies;

	public DependencyExtractor() {
		this.dependencies = new HashSet<>();
	}

	/**
	 * Extracts all the locals used in a given expression.
	 *
	 * @param expression
	 *            The expression to be scanned.
	 * @return The {@link Local} subexpressions present in the given expression.
	 */
	public Set<Value> visit(final SootExpression expression) {
		final Collection<ValueBox> useBoxes = expression.getUseBoxes();
		expression.apply(this);

		for (ValueBox useBox : useBoxes) {
			useBox.getValue().apply(this);
		}

		return dependencies;
	}

	@Override
	public void caseLocal(final Local local) {
		dependencies.add(new CachedEquivalentValue(local));
	}

	@Override
	public void caseInstanceFieldRef(final InstanceFieldRef reference) {
		dependencies.add(new CachedEquivalentValue(reference));
	}

	@Override
	public void caseStaticFieldRef(final StaticFieldRef reference) {
		dependencies.add(new CachedEquivalentValue(reference));
	}

	@Override
	public void caseArrayRef(final ArrayRef reference) {
		dependencies.add(new CachedEquivalentValue(reference));
	}

}
