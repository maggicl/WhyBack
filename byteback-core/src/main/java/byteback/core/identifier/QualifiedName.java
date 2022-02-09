package byteback.core.identifier;

import java.util.Arrays;
import java.util.List;

public class QualifiedName {

    /**
     * Validates a single name.
     *
     * @return {@code true} if the name is valid.
     */
    private static boolean validatePart(String part) {
        return part.length() > 0 && Character.isJavaIdentifierStart(part.charAt(0))
                && part.chars().allMatch(Character::isJavaIdentifierPart);
    }

    /**
     * Parts of the qualified name.
     */
    protected final List<String> parts;

    /**
     * Constructs a qualified name.
     *
     * @param parts The parts of the qualified name.
     */
    public QualifiedName(String... parts) {
        this.parts = List.of(parts);
    }

    /**
     * @param name The string representation of the qualified name.
     * @see #QualifiedName(String...)
     */
    public QualifiedName(String name) {
        this.parts = Arrays.asList(name.split("\\."));
    }

    /**
     * Validates the qualified name.
     *
     * @return {@code true} if the qualified name follows a valid form.
     */
    public boolean validate() {
        return parts.stream().allMatch(QualifiedName::validatePart);
    }

}
