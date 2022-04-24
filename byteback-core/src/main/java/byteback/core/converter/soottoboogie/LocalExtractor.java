package byteback.core.converter.soottoboogie;

import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import soot.Local;
import soot.Value;

/**
 * Extractor class for a {@link Local} expression.
 *
 * @author paganma
 */
public class LocalExtractor extends SootExpressionVisitor<Local> {

	private Local local;

	/**
	 * Extracts a local expected from an input expression.
	 *
	 * @param expression
	 *            The input expression.
	 * @return The expected {@link Local} expression.
	 * @throws ConversionException
	 *             If the input expression does not include a {@link Local}.
	 */
	public Local visit(final SootExpression expression) {
		expression.apply(this);

		return result();
	}

	@Override
	public void caseLocal(final Local local) {
		this.local = local;
	}

	@Override
	public void caseDefault(final Value expression) {
		throw new ConversionException("Expected local definition, got " + expression);
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
