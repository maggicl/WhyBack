package byteback.whyml.syntax.function;

import java.util.List;

public record WhyFunctionContract(
		WhyFunctionSignature signature,
		List<WhyCondition> conditions) implements Comparable<WhyFunctionContract> {
	@Override
	public int compareTo(WhyFunctionContract o) {
		return this.signature.compareTo(o.signature);
	}
}
