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
