package byteback.core.converter.soot.boogie;

import byteback.frontend.boogie.ast.Expression;
import java.util.Optional;
import soot.Local;

public class SubstitutingExtractor extends ExpressionExtractor {

	private final Substituter substituter;

	public SubstitutingExtractor(final Substituter substituter) {
		this.substituter = substituter;
	}

	@Override
	public void caseLocal(final Local local) {
		final Optional<Expression> substitution = substituter.substitute(local);
		substitution.ifPresentOrElse(this::pushExpression, () -> super.caseLocal(local));
	}

}
