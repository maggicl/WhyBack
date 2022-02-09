package byteback.core.identifier;

import byteback.core.identifier.QualifiedName;

/**
 * Represents the qualified name of a class.
 */
public class ClassName extends QualifiedName {

    /**
     * Constructs a classname from its parts.
     *
     * @param parts Qualified path ending with a legal class name.
     */
    public ClassName(String... parts) {
        super(parts);
    }

    /**
     * Constructs a classname from its parts.
     *
     * @param name String representing the qualified path.
     */
    public ClassName(String name) {
        super(name);
    }

    /**
     * Validates the classname.
     *
     * @return {@code true} if the classname could be validated.
     */
    @Override
    public boolean validate() {
        if (!super.validate()) {
            return false;
        }
        
        boolean classEncountered = false;

        for (String part : parts) {
            final boolean isClass = Character.isUpperCase(part.charAt(0));

            if (isClass) {
                classEncountered = true;
            } else if (classEncountered) {
                return false;
            }
        }

        return classEncountered;
    }

    @Override
    public String toString() {
        return String.join(".", parts);
    }

}
