package byteback.core.identifier;

import javax.lang.model.SourceVersion;

/**
 * Represents the qualified name of a class.
 */
public class QualifiedName {

    private final String name;

    /**
     * Constructs a qualified name from its parts.
     *
     * @param parts The parts of the name.
     * @return The {@code QualifiedName} instance.
     * @throws IllegalArgumentException if the name is not valid.
     */
    public static QualifiedName get(final String... parts) {
        final String name = String.join(".", parts);

        return QualifiedName.get(name);
    }

    /**
     * Constructs a qualified name from a string.
     *
     * @param name A string representing the qualified name.
     * @return The {@code QualifiedName} instance.
     * @throws IllegalArgumentException if the name is not valid.
     */
    public static QualifiedName get(final String name) {
        final QualifiedName instance = new QualifiedName(name);

        if (!instance.validate()) {
            throw new IllegalArgumentException("Invalid name " + instance.name);
        }

        return instance;
    }

    /**
     * Constructs a classname from its parts.
     *
     * @param name String representing the qualified path.
     */
    private QualifiedName(final String name) {
        this.name = name;
    }

    /**
     * Checks if the qualified name is valid using the {@link SourceVersion} utility
     * class.
     *
     * @return {@code true} if the name is valid.
     */
    private boolean validate() {
        return SourceVersion.isName(name);
    }

    /**
     * Checks if the qualified name starts with the given name.
     *
     * @param qualifiedName The given qualified name.
     * @return {@code true} if this name is prefixed by the given qualified name.
     */
    public boolean startsWith(final QualifiedName qualifiedName) {
        return startsWith(qualifiedName.toString());
    }

    /**
     * Checks if the qualified name starts with the given string.
     *
     * @param name The given name string.
     * @return {@code true} if this name is prefixed by the given name.
     */
    public boolean startsWith(final String name) {
        return this.name.startsWith(name);
    }

    /**
     * Getter for the string representation of the qualified name.
     *
     * @return The string representation of the qualified name.
     */
    @Override
    public String toString() {
        return name;
    }

}
