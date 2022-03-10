package byteback.core.representation.body.soot;

import byteback.core.Visitable;
import soot.jimple.Expr;

public class SootExpression implements Visitable<SootExpressionVisitor> {

    private final Expr sootExpression;

    public SootExpression(final Expr sootExpression) {
        this.sootExpression = sootExpression;
    }

    @Override
    public void apply(final SootExpressionVisitor visitor) {
        sootExpression.apply(visitor);
    }

}