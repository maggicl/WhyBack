package byteback.core.context;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the qualified name of a class.
 */
public class QualifiedName {

    /**
     * Validates a single name.
     *
     * @return {@code true} if the name is valid.
     */
    private static boolean validatePart(String part) {
        return part.length() > 0
            && Character.isJavaIdentifierStart(part.charAt(0))
            && part.chars().allMatch(Character::isJavaIdentifierPart);
    }

    /**
     * Parts of the qualified name.
     */
    private final List<String> parts;

    /**
     * Constructs a qualified name.
     *
     * @param parts The parts of the qualified name.
     */
    public QualifiedName(String... parts) {
        this.parts = List.of(parts);
    }

    /**
     * Validates the qualified name.
     *
     * @return {@code true} if the qualified name follows a valid form.
     */
    public boolean validate() {
        boolean classEncountered = false;

        for (String part : parts) {
            if (validatePart(part)) {
                final boolean isClass = Character.isUpperCase(part.charAt(0));

                if (isClass) {
                    classEncountered = true;
                } else if (classEncountered) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return classEncountered;
    }

    @Override
    public String toString() {
        return parts.stream().collect(Collectors.joining("."));
    }

}

