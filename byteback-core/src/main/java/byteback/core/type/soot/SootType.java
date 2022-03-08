package byteback.core.type.soot;

import byteback.core.type.Type;

/**
 * Encapsulates a Soot type representation, visitable using a
 * {@link SootTypeVisitor}.
 *
 * This type will eventually also be used as the base type for all Soot types
 * wrappers. For now this wrapping is not considered necessary, as there are no
 * features that are planned to be added to the Soot type hierarchy.
 */
public class SootType implements Type<SootTypeVisitor> {

    private final soot.Type sootType;

    /**
     * Constructs a {@link SootType} from a {@link soot.Type} instance.
     *
     * @param sootType The given {@link soot.Type} instance.
     */
    public SootType(final soot.Type sootType) {
        this.sootType = sootType;
    }

    /**
     * Getter for the number of the type.
     *
     * This number is given dynamically by the underlying {@link soot.Scene}, and
     * should not be assumed to be always the same across multiple contexts.
     *
     * @return The number associated with this type.
     */
    public int getNumber() {
        return sootType.getNumber();
    }

    /**
     * Applies the {@link SootTypeVisitor} to the wrapped {@link soot.Type}
     * instance.
     *
     * @param visitor The visitor to be applied.
     */
    @Override
    public void apply(final SootTypeVisitor visitor) {
        sootType.apply(visitor);
    }

    /**
     * Checks the equality of two types based on their number.
     *
     * @param object The given object instance to be checked against this instance.
     * @return {@code true} if the given object is a {@link SootType} and has the
     *         same number as this instance.
     */
    @Override
    public boolean equals(Object object) {
        return object instanceof SootType && getNumber() == ((SootType) object).getNumber();
    }

}
