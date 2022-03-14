package byteback.core.converter.soot.boogie;

import java.util.Map;
import java.util.Optional;

import byteback.frontend.boogie.ast.Expression;
import soot.Local;

public class BoogieInlineExtractor extends BoogieExpressionExtractor {

    private final Map<Local, Optional<Expression>> localExpressionIndex;

    public BoogieInlineExtractor(final Map<Local, Optional<Expression>> localExpressionIndex) {
        this.localExpressionIndex = localExpressionIndex;
    }

    @Override
    public BoogieInlineExtractor instance() {
        return new BoogieInlineExtractor(localExpressionIndex);
    }

    @Override
    public void caseLocal(final Local local) {
        final Optional<Expression> expressionOptional = localExpressionIndex.get(local);
        expressionOptional.ifPresentOrElse(this::setExpression, () -> super.caseLocal(local));
    }

}
