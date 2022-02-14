package byteback.core.identifier;

import javax.lang.model.SourceVersion;

/**
 * Represents the qualified name of an entity.
 */
public class Name {

    private final String name;

    /**
     * Constructs a qualified name from its parts.
     *
     * @param parts The parts of the name.
     * @return The {@code QualifiedName} instance.
     * @throws IllegalArgumentException if the name is not valid.
     */
    public static Name get(final String... parts) {
        final String name = String.join(".", parts);

        return Name.get(name);
    }

    /**
     * Constructs a qualified name from a string.
     *
     * @param name A string representing the qualified name.
     * @return The {@code QualifiedName} instance.
     * @throws IllegalArgumentException if the name is not valid.
     */
    public static Name get(final String name) {
        final Name instance = new Name(name);

        if (!instance.validate()) {
            throw new IllegalArgumentException("Invalid qualified name " + instance.name);
        }

        return instance;
    }

    /**
     * Constructs a classname from its string representation.
     *
     * @param name String representing the qualified path.
     */
    Name(final String name) {
        this.name = name;
    }

    /**
     * Checks if the qualified name is valid using the {@link SourceVersion} utility
     * class.
     *
     * @return {@code true} if the name is valid.
     */
    boolean validate() {
        return SourceVersion.isName(name);
    }

    /**
     * Checks if the qualified name starts with the given name.
     *
     * @param name The given qualified name.
     * @return {@code true} if this name is prefixed by the given qualified name.
     */
    public boolean startsWith(final Name name) {
        return startsWith(name.toString());
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
