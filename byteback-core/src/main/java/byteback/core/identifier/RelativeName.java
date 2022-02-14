package byteback.core.identifier;

import soot.Type;

/**
 * Represents a simple relative name, taken outside the context of any
 * namespace.
 */
public class RelativeName extends QualifiedName {

    /**
     * Constructs a relative name from a string.
     *
     * @param name A string representing the relative name.
     * @return The {@code SimpleName} instance.
     * @throws IllegalArgumentException if the name is not valid.
     */
    public static RelativeName get(final String name) {
        final RelativeName instance = new RelativeName(name);

        if (!instance.validate()) {
            throw new IllegalArgumentException("Invalid simple name " + name);
        }

        return instance;
    }

    /**
     * Checks if the relative name is valid by using the {@link QualifiedName}
     * preconditions and the absence of a namespace.
     *
     * @return {@code true} if the name is valid.
     */
    @Override
    boolean validate() {
        Type
        return toString().split("\\.").length == 1 && super.validate();
    }

    /**
     * Constructs the relative name from its string representation.
     * 
     * @param name String representing the relative name.
     */
    RelativeName(String name) {
        super(name);
    }

}
