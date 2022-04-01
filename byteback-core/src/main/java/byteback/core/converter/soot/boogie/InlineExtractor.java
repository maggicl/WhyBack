package byteback.core.converter.soot.boogie;

import byteback.frontend.boogie.ast.Expression;
import java.util.Map;
import java.util.Optional;
import soot.Local;

public class InlineExtractor extends ExpressionExtractor {

	private final Map<Local, Optional<Expression>> expressionTable;

	public InlineExtractor(final Map<Local, Optional<Expression>> expressionTable) {
		this.expressionTable = expressionTable;
	}

	@Override
	public void caseLocal(final Local local) {
		final Optional<Expression> expression = expressionTable.getOrDefault(local, Optional.empty());
		expression.ifPresentOrElse(this::pushExpression, () -> super.caseLocal(local));
	}

}
