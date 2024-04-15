package byteback.whyml.syntax.function;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record WhyFunctionSignature(
		VimpMethod vimp,
		Optional<WhyFunctionParam> thisParam,
		List<WhyFunctionParam> paramsList,
		WhyType returnType,
		WhyFunctionKind.Inline inline,
		List<VimpCondition> conditions) {

	public Stream<WhyFunctionParam> params() {
		return Stream.concat(thisParam.stream(), paramsList.stream());
	}

	public int paramCount() {
		return (thisParam.isPresent() ? 1 : 0) + paramsList.size();
	}

	public Stream<WhyFunctionParam> paramsWithResult(Identifier.L resultParamName) {
		if (returnType == WhyJVMType.UNIT) return params();
		return Stream.concat(params(), Stream.of(new WhyFunctionParam(resultParamName, returnType, false)));
	}

	public WhyFunctionKind kind() {
		return vimp.kind(inline);
	}
}
