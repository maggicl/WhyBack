package byteback.whyml.delta;

public interface IOracle {
    /**
     * Returns "true" if test input fails the test in the desired way
     * @param input the test input
     */
    boolean isFailing(TestInput input);
}
