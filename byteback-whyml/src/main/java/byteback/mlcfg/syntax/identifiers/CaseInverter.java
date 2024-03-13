package byteback.mlcfg.syntax.identifiers;

public class CaseInverter {
	public int invertCase(int from) {
		return switch (Character.getType(from)) {
			case Character.LOWERCASE_LETTER -> Character.toUpperCase(from);
			case Character.UPPERCASE_LETTER -> Character.toLowerCase(from);
			default -> from;
		};
	}

	public String invertCase(String from) {
		final int[] codePoints = from.codePoints().toArray();
		codePoints[0] = invertCase(codePoints[0]);
		return new String(codePoints, 0, codePoints.length);
	}
}
