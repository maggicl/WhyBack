package byteback.mlcfg.syntax;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.types.WhyReference;
import byteback.mlcfg.syntax.types.WhyType;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record WhyMethod(
		Identifier.L name,
		Identifier.FQDN declaringClass,
		boolean isStatic,
		List<WhyType> paramTypes,
		Optional<WhyType> returnType) {
	public Stream<WhyMethodParam> params() {
		final Optional<WhyMethodParam> thisParam = isStatic
				? Optional.empty()
				: Optional.of(new WhyMethodParam(Identifier.thisParam(), new WhyReference(declaringClass), true));

		final Stream<WhyMethodParam> params = IntStream.range(0, paramTypes.size())
				.mapToObj(i -> new WhyMethodParam(Identifier.methodParam(i), paramTypes.get(i), false));

		return Stream.concat(thisParam.stream(), params);
	}
}
