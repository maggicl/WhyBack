package byteback.whyml;

import java.util.Collections;
import java.util.stream.Stream;

public final class Utils {
	private Utils() {
	}

	public static Stream<String> repeat(int times, String s) {
		return Collections.nCopies(times, s).stream();
	}

	public static String trimToNull(String e) {
		return e.isEmpty() ? null : e;
	}
}
