package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.type.SootType;
import byteback.frontend.boogie.ast.Expression;
import java.util.Map;
import java.util.Optional;
import soot.Local;

public class InlineExtractor extends ExpressionExtractor {

	private final Map<Local, Optional<Expression>> expressionIndex;

	public InlineExtractor(final SootType type, final Map<Local, Optional<Expression>> expressionIndex) {
		super(type);
		this.expressionIndex = expressionIndex;
	}

	@Override
	public void caseLocal(final Local local) {
		final Optional<Expression> expression = expressionIndex.getOrDefault(local, Optional.empty());
		expression.ifPresentOrElse(this::pushExpression, () -> super.caseLocal(local));
	}

}
