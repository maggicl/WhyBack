package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.type.SootType;
import byteback.frontend.boogie.ast.Expression;
import java.util.Map;
import java.util.Optional;
import soot.Local;

public class BoogieInlineExtractor extends BoogieExpressionExtractor {

	private final Map<Local, Optional<Expression>> expressionIndex;

	public BoogieInlineExtractor(final SootType type, final Map<Local, Optional<Expression>> expressionIndex) {
		super(type);
		this.expressionIndex = expressionIndex;
	}

	@Override
	public BoogieInlineExtractor subExpressionExtractor(final SootType type) {
		return new BoogieInlineExtractor(type, expressionIndex);
	}

	@Override
	public void caseLocal(final Local local) {
		final Optional<Expression> expression = expressionIndex.get(local);
		expression.ifPresentOrElse(this::setExpression, () -> super.caseLocal(local));
	}

}
