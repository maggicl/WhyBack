package byteback.mlcfg.syntax;

import byteback.mlcfg.syntax.types.WhyType;
import byteback.mlcfg.vimp.VimpFunctionReference;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record WhyFunctionSignature(
		VimpFunctionReference reference,
		Optional<WhyFunctionParam> thisParam,
		List<WhyFunctionParam> paramsList,
		WhyType returnType,
		List<WhyCondition> conditions) {

	public Stream<WhyFunctionParam> params() {
		return Stream.concat(thisParam.stream(), paramsList.stream());
	}

	public WhyFunctionKind kind() {
		return reference.kind();
	}
}
