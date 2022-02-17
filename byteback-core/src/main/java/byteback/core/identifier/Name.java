package byteback.core.identifier;

import javax.lang.model.SourceVersion;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the qualified name of an entity.
 */
public class Name {

    private final String name;

    /**
     * Constructs a classname from its string representation.
     *
     * @param name String representing the qualified path.
     */
    public Name(final String name) {
        this.name = name;

        assert validate();
    }

    /**
     * Checks if the qualified name is valid using the {@link SourceVersion} utility
     * class.
     *
     * @return {@code true} if the name is valid.
     */
    public boolean validate() {
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
     * Getter for the parts composing the name.
     *
     * @return The parts composing the name as a list.
     */
    public List<String> getParts() {
        return Arrays.asList(name.split("\\."));
    }

    /**
     * Getter for the head of the name.
     *
     * @return The final part of the name.
     */
    public Name getHead() {
        final List<String> parts = getParts();

        return new Name(parts.get(parts.size() - 1));
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

    @Override
    public boolean equals(Object object) {
        return object instanceof Name && ((Name)object).toString().equals(name);
    }

}
