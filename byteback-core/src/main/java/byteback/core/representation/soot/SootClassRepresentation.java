package byteback.core.representation.soot;

import java.util.List;
import java.util.stream.Stream;

import byteback.core.identifier.Name;
import byteback.core.representation.ClassRepresentation;
import byteback.core.visitor.type.soot.SootType;
import soot.SootClass;

/**
 * Wraps a {@link SootClass} intermediate representation.
 */
public class SootClassRepresentation implements ClassRepresentation {

    private final SootClass sootClass;

    private final Name name;

    /**
     * Constructor the Soot class representation wrapper.
     *
     * @param sootClass The wrapped {@link SootClass} class.
     */
    public SootClassRepresentation(final SootClass sootClass) {
        this.sootClass = sootClass;
        this.name = new Name(sootClass.getName());
    }

    /**
     * Verifies if the class is part of the basic classes set.
     *
     * The original Soot implementation performs simple checks on the prefix of the
     * package. Oddly enough the list did not include the "jdk." prefix.
     *
     * @return {@code true} if the instance refers to a basic class.
     */
    public boolean isBasicClass() {
        return sootClass.isJavaLibraryClass() || name.startsWith("jdk");
    }

    /**
     * Verifies if the class is a phantom class. A phantom class is a class that
     * does not exist in the classpath.
     *
     * @return {@code true} if the instance refers to a phantom class.
     */
    public boolean isPhantomClass() {
        return sootClass.isPhantomClass();
    }

    /**
     * Getter for the qualified name of the class.
     *
     * @return The qualified name of the class.
     */
    @Override
    public Name getName() {
        return name;
    }

    /**
     * Getter for the type corresponding to this class.
     *
     * @return The type corresponding to the {@link SootClass}.
     */
    public SootType getType() {
        return new SootType(sootClass.getType());
    }

    /**
     * Yields the stream of Soot methods present in the class.
     *
     * @return The stream of method representations.
     */
    public Stream<SootMethodRepresentation> methods() {
        assert !isPhantomClass();

        return sootClass.getMethods().stream().map(SootMethodRepresentation::new);
    }

    /**
     * Yields the stream of Soot fields present in the class.
     *
     * @return The stream of field representations.
     */
    public Stream<SootFieldRepresentation> fields() {
        assert !isPhantomClass();

        throw new UnsupportedOperationException();
    }

}
