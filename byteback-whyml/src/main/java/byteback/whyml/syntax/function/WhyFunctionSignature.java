package byteback.whyml.syntax.function;

import byteback.whyml.syntax.type.WhyType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record WhyFunctionSignature(
		VimpMethod vimp,
		Optional<WhyFunctionParam> thisParam,
		List<WhyFunctionParam> paramsList,
		WhyType returnType,
		List<VimpCondition> conditions) {

	public Stream<WhyFunctionParam> params() {
		return Stream.concat(thisParam.stream(), paramsList.stream());
	}

	public WhyFunctionKind kind() {
		return vimp.kind();
	}
}
