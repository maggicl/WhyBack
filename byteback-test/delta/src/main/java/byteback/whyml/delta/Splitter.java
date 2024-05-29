package byteback.whyml.delta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Splitter implements ISplitter {
	public Result split(TestInput input, int nChunks) {
		Objects.requireNonNull(input);

		if (input.length() < nChunks) {
			throw new IllegalArgumentException("number of splits larger than input: " + input.length() + " " + nChunks);
		}

		final List<TestInput> chunks = new ArrayList<>();
		double chunkSize = input.length() / (double) nChunks;

		for (int i = 0; i < nChunks; i++) {
			int from = (int) Math.floor(chunkSize * i);
			int to = (int) Math.floor(chunkSize * (i + 1));
			chunks.add(input.subInput(from, to));
		}

		if (chunks.size() != nChunks) {
			throw new IllegalStateException("number of chunks different from length of list of chunks: " + chunks.size() + " " + nChunks);
		}

		return new Result(IntStream.range(0, nChunks).parallel().mapToObj(chunks::get),
				IntStream.range(0, nChunks)
						.parallel()
						.mapToObj(i -> IntStream.range(0, nChunks).filter(j -> j != i)
						.mapToObj(chunks::get)
						.reduce(TestInput::add))
						.flatMap(Optional::stream));
	}
}
