package byteback.whyml.delta;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class DeltaDebugger {
    private final IOracle testOracle;

    private final ISplitter splitter;
    
    private final PrintStream logger;

    public DeltaDebugger(IOracle testOracle, ISplitter splitter, PrintStream logger) {
        this.testOracle = testOracle;
        this.splitter = splitter;
        this.logger = logger;
    }

    private Optional<TestInput> findFailing(Stream<TestInput> inputs) {
        return inputs.filter(testOracle::isFailing).findAny();
    }

    public TestInput execute(TestInput input, int initialN) {
        TestInput testInput = input;
        int n = initialN;

        while (true) {
//            logger.println("current test case: " + testInput);
			logger.printf("chars: %d, length: %d, n: %d%n", testInput.chars(), testInput.length(), n);

            final ISplitter.Result splits = splitter.split(testInput, n);
            final Optional<TestInput> failingDelta = findFailing(splits.deltas());

            if (failingDelta.isPresent()) {
                logger.println("A case taken");

                testInput = failingDelta.get();

                if (testInput.length() < 2) {
                    break;
                }
                n = 2;
            }

            final Optional<TestInput> failingNabla = findFailing(splits.nablas());

            if (failingNabla.isPresent()) {
                logger.println("B case taken");

                testInput = failingNabla.get();
                n = n - 1;
            } else if (n * 2 <= testInput.length()) {
                logger.println("C1 case taken");

                n = n * 2;
            } else {
                break;
            }
        }

        return testInput;
    }
}
