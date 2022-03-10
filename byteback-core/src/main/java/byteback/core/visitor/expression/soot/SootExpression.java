package byteback.core.visitor.expression.soot;

import byteback.core.visitor.expression.Expression;
import soot.jimple.Expr;

public class SootExpression implements Expression<SootExpressionVisitor> {

    private final Expr sootExpression;

    public SootExpression(final Expr sootExpression) {
        this.sootExpression = sootExpression;
    }

    @Override
    public void apply(final SootExpressionVisitor visitor) {
        sootExpression.apply(visitor);
    }

}
