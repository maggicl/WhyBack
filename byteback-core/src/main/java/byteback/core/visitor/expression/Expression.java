package byteback.core.visitor.expression;

import byteback.core.visitor.Visitable;

public interface Expression<T extends ExpressionVisitor> extends Visitable<T> {
}
