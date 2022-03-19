package byteback.core.converter.soot.boogie;

import java.util.Map;
import java.util.Optional;

import byteback.core.representation.soot.type.SootType;
import byteback.frontend.boogie.ast.Expression;
import soot.Local;

public class BoogieInlineExtractor extends BoogieExpressionExtractor {

    private final Map<Local, Optional<Expression>> localExpressionIndex;

    public BoogieInlineExtractor(final SootType type, final Map<Local, Optional<Expression>> localExpressionIndex) {
        super(type);
        this.localExpressionIndex = localExpressionIndex;
    }

    @Override
    public BoogieInlineExtractor subExpressionExtractor(final SootType type) {
        return new BoogieInlineExtractor(type, localExpressionIndex);
    }

    @Override
    public void caseLocal(final Local local) {
        final Optional<Expression> expression = localExpressionIndex.get(local);
        expression.ifPresentOrElse(this::setExpression, () -> super.caseLocal(local));
    }

}
