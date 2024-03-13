package byteback.mlcfg;

import byteback.mlcfg.syntax.WhyProgram;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class Utils {
	private Utils() {}

	public static String indent(String toIndent, int levels) {
		return Arrays.stream(toIndent.split("\\n"))
				.map(e -> WhyProgram.INDENT.repeat(levels) + e)
				.collect(Collectors.joining("\n"));
	}
}
