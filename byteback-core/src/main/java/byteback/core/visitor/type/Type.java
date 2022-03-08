package byteback.core.visitor.type;

import byteback.core.visitor.Visitable;

/**
 * Base interface for a type entity. Every type should be accessed using a
 * visitor.
 *
 * @param <T> The type of the visitor used to access the type representation.
 */
public interface Type<T extends TypeVisitor> extends Visitable<T> {
}