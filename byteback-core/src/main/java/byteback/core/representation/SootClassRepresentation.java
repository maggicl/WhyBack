package byteback.core.representation;

import java.util.stream.Stream;

import byteback.core.identifier.ClassName;
import soot.SootClass;

/**
 * Wraps a {@code SootClass} intermediate representation.
 */
public class SootClassRepresentation implements ClassRepresentation<SootMethodRepresentation, FieldRepresentation> {

    private final SootClass sootClass;

    private final ClassName name;

    /**
     * @param sootClass The wrapped {@code SootClass} class.
     */
    public SootClassRepresentation(final SootClass sootClass) {
        this.sootClass = sootClass;
        this.name = new ClassName(sootClass.getName());
    }

    /**
     * @return {@code true} if the class refers to a basic class.
     */
    public boolean isLibraryClass() {
        return sootClass.isLibraryClass();
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
        return sootClass.isJavaLibraryClass() || name.isPrefixedBy("jdk");
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
     * @return The qualified name of the class.
     */
    @Override
    public ClassName getName() {
        return name;
    }

    /**
     * Computes the method representation from the soot methods present in the
     * class.
     *
     * @return The stream of method representations.
     */
    @Override
    public Stream<SootMethodRepresentation> methods() {
        assert !isPhantomClass();

        return sootClass.getMethods().stream().map(SootMethodRepresentation::new);
    }

    @Override
    public Stream<FieldRepresentation> fields() {
        assert !isPhantomClass();

        throw new UnsupportedOperationException();
    }

}
