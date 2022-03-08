package byteback.core.type;

/**
 * Base interface for a type entity. Every type should be accessed using a
 * visitor.
 *
 * @param <T> The type of the visitor used to access the type representation.
 */
public interface Type<T extends TypeVisitor> {

    void apply(T visitor);

}
