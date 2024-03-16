package byteback.mlcfg.syntax;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.types.WhyReference;
import byteback.mlcfg.syntax.types.WhyType;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record WhyFunction(
		Identifier.L name,
		Identifier.FQDN declaringClass,
		WhyFunctionKind kind,
		List<WhyType> paramTypes,
		Optional<WhyType> returnType) {

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
						Identifier.Special.methodParam(i + 1),
						paramTypes.get(i),
						false));

		return Stream.concat(thisParam.stream(), params);
	}
}
