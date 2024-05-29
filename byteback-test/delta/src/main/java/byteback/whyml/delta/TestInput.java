package byteback.whyml.delta;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record TestInput(List<String> chunks, String separator) {
	public static TestInput fromString(String input, String separator) {
		return new TestInput(Arrays.asList(input.split(separator)), separator);
	}

	public void writeToFile(Path path) throws IOException {
		try (final BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
			for (int i = 0; i < chunks.size(); i++) {
				final String chunk = chunks.get(i);

				if (i > 0) bw.write(separator);
				bw.write(chunk);
			}
		}
	}

	public int length() {
		return chunks.size();
	}

	public int chars() {
		return chunks().stream().mapToInt(String::length).sum();
	}

	public TestInput subInput(int from, int to) {
		return new TestInput(chunks.subList(from, to), separator);
	}

	public TestInput add(TestInput b) {
		final ArrayList<String> chunks = new ArrayList<>(this.chunks);
		chunks.addAll(b.chunks);
		return new TestInput(chunks, separator);
	}

	@Override
	public String toString() {
		return String.join(separator, chunks);
	}
}
