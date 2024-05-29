package byteback.whyml.delta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
	public static void main(String[] args) throws IOException {
//        final Path testPath = Path.of(args[1]);
		final Path testPath = Path.of(
				"/Users/maggicl/git/byteback/byteback-test/test-java-8/src/main/java/byteback/test/library/java/util/Output/LinkedList.java.tmp.mlcfg"
		);

		final Splitter splitter = new Splitter();
		final WhyMLOracle oracle = new WhyMLOracle(testPath);
		final DeltaDebugger debugger = new DeltaDebugger(oracle, splitter, System.err);

		final String separator = "\n\n";
		String minimal = Files.readString(testPath);
		int len, i = 0;

		do {
			len = minimal.length();
			System.err.printf(
					"Iteration %d, separator is: %s, length %s%n",
					i++,
					separator.replaceAll("\n", "\\\\n"),
					minimal.length()
			);
			minimal = debugger.execute(TestInput.fromString(minimal, separator), 2).toString();
		} while (len > minimal.length());

		final TestInput t = TestInput.fromString(minimal, separator);
		minimal = debugger.execute(t, t.length()).toString();

		Files.writeString(
				testPath.resolveSibling(
						testPath.getFileName().toString()
								.replaceAll("\\.mlcfg$", ".minimal.mlcfg")),
				minimal
		);
	}
}
