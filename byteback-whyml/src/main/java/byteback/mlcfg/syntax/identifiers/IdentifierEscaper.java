package byteback.mlcfg.syntax.identifiers;

import java.util.stream.Collectors;

public class IdentifierEscaper {
	public String escape(String input) {
		return input.codePoints().mapToObj(e -> {
			if (Character.isLetterOrDigit(e)) {
				return Character.toString(e);
			} else if (e == '_') {
				return "__"; // underscores are doubled
			} else {
				return String.format("_u%04X", e); // all other characters are encoded as unicode codepoints
			}
		}).collect(Collectors.joining(""));
	}
}