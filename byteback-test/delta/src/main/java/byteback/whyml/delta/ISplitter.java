package byteback.whyml.delta;

import java.util.stream.Stream;

public interface ISplitter {
	Result split(TestInput input, int nChunks);

	record Result(Stream<TestInput> deltas, Stream<TestInput> nablas) {
	}
}
