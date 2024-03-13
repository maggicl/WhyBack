package byteback.mlcfg;

import byteback.mlcfg.syntax.WhyProgram;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Utils {
	private Utils() {}

	public static String indent(String toIndent, int levels) {
		return Arrays.stream(toIndent.split("\\n"))
				.map(e -> WhyProgram.INDENT.repeat(levels) + e)
				.collect(Collectors.joining("\n"));
	}

	public static Stream<String> repeat(int times, String s) {
		return Collections.nCopies(times, s).stream();
	}
}
