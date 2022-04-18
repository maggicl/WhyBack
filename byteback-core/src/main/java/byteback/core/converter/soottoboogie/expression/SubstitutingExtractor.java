package byteback.core.converter.soottoboogie.expression;

import byteback.frontend.boogie.ast.Expression;
import java.util.Optional;
import soot.Local;

public class SubstitutingExtractor extends ExpressionExtractor {

	private final Substitutor substitutor;

	public SubstitutingExtractor(final Substitutor substitutor) {
		this.substitutor = substitutor;
	}

	public Substitutor getSubstitutor() {
		return substitutor;
	}

	@Override
	public void caseLocal(final Local local) {
		final Optional<Expression> substitution = substitutor.substitute(local);
		substitution.ifPresentOrElse((expression) -> pushCastExpression(expression, local),
				() -> super.caseLocal(local));
	}

}
