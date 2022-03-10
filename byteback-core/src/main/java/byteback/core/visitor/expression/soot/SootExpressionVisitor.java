package byteback.core.visitor.expression.soot;

import byteback.core.visitor.expression.ExpressionVisitor;
import soot.jimple.AbstractJimpleValueSwitch;

/**
 * Base class for a {@link SootExpression} visitor.
 */
public abstract class SootExpressionVisitor extends AbstractJimpleValueSwitch implements ExpressionVisitor {
}
