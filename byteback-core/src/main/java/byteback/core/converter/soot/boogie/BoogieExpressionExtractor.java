package byteback.core.converter.soot.boogie;

import byteback.core.representation.body.soot.SootExpressionVisitor;
import byteback.frontend.boogie.ast.Expression;
import soot.jimple.Expr;
import soot.jimple.StaticInvokeExpr;

public class BoogieExpressionExtractor extends SootExpressionVisitor {

    private Expression expression;

    @Override
    public void caseStaticInvokeExpr(final StaticInvokeExpr invocation) {
    }

    @Override
    public void caseDefault(final Expr expression) {
        throw new UnsupportedOperationException("Unable to convert Jimple expression of type " + expression.getClass() + " to Boogie");
    }

    @Override
    public Expression getResult() {
        return expression;
    }

}
