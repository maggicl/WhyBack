package byteback.mlcfg.syntax;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.types.WhyReference;
import byteback.mlcfg.syntax.types.WhyType;
import byteback.mlcfg.vimp.VimpFunctionReference;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record WhyFunctionSignature(
		Identifier.L name,
		Identifier.FQDN declaringClass,
		Identifier.L specName,
		WhyFunctionKind kind,
		List<WhyType> paramTypes,
		WhyType returnType,
		List<WhyCondition> conditions) {

	public Stream<WhyFunctionParam> params() {
		final Optional<WhyFunctionParam> thisParam;
		thisParam = kind != WhyFunctionKind.INSTANCE_METHOD
				? Optional.empty()
				: Optional.of(new WhyFunctionParam(
				Identifier.Special.THIS,
				new WhyReference(declaringClass),
				true));

		final Stream<WhyFunctionParam> params = IntStream.range(0, paramTypes.size())
				.mapToObj(i -> new WhyFunctionParam(
						Identifier.Special.methodParam(i),
						paramTypes.get(i),
						false));

		return Stream.concat(thisParam.stream(), params);
	}

	public String identifier() {
		return "%s.%s".formatted(declaringClass, name);
	}
}
