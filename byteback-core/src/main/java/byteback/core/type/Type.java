package byteback.core.type;

import byteback.core.Visitable;
import byteback.core.Visitor;

/**
 * Base interface for a type entity. Every type should be accessed using a
 * visitor.
 *
 * @param <T> The type of the visitor used to access the type representation.
 */
public interface Type<T extends Visitor> extends Visitable<T> {
}
