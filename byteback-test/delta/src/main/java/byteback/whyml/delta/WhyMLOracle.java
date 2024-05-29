package byteback.whyml.delta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class WhyMLOracle implements IOracle {
	private final String output;
	private static final AtomicInteger fileCounter = new AtomicInteger(0);

	public WhyMLOracle(Path toTest) {
		this.output = runWhy(toTest);
		System.out.println("Searching:\n" + this.output);
	}

	private String runWhy(Path path) {
		Runtime rt = Runtime.getRuntime();

		final String[] commands = {
				"/Users/maggicl/git/byteback/byteback-test/verify.sh",
				path.toAbsolutePath().toString().replaceAll("\\.mlcfg$", "")
		};

		try {
			Process proc = rt.exec(commands);

			Scanner stdout = new Scanner(proc.getInputStream()).useDelimiter("\\A");
			Scanner stderr = new Scanner(proc.getErrorStream()).useDelimiter("\\A");

			return "STDOUT:\n" +
					(stdout.hasNext() ? stdout.next() : "") +
					"\nSTDERR:\n" +
					(stderr.hasNext() ? stderr.next() : "");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isFailing(TestInput input) {
		Path tmp = null;
		try {
			tmp = Path.of("/Volumes/RAMDisk/%d.mlcfg".formatted(fileCounter.incrementAndGet()));
			input.writeToFile(tmp);
			final String output = runWhy(tmp);

			return this.output.equals(output);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (tmp != null) {
				try {
					Files.delete(tmp);
				} catch (IOException ignored) {
				}
			}
		}
	}
}
