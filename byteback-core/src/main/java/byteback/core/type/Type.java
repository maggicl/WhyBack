package byteback.core.type;

import byteback.core.Visitable;
import byteback.core.Visitor;

public interface Type<T extends Visitor> extends Visitable<T> {
}