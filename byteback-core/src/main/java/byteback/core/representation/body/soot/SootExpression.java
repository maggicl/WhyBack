package byteback.core.representation.body.soot;

import byteback.core.representation.Visitable;
import soot.Value;

public class SootExpression implements Visitable<SootExpressionVisitor<?>> {

    private final Value sootExpression;

    public SootExpression(final Value sootExpression) {
        this.sootExpression = sootExpression;
    }

    @Override
    public void apply(final SootExpressionVisitor<?> visitor) {
        sootExpression.apply(visitor);
    }

}
