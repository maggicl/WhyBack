package byteback.core.representation.body.soot;

import byteback.core.representation.Visitor;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.Expr;

/**
 * Base class for a {@link SootExpression} visitor.
 */
public abstract class SootExpressionVisitor extends AbstractJimpleValueSwitch implements Visitor<Expr> {
}
