package byteback.mlcfg.syntax.types;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WhyArrayType implements WhyType {
	private final WhyType baseType;

	public WhyArrayType(WhyType baseType) {
		this.baseType = baseType;
	}

	@Override
	public Optional<String> getPrefix() {
		return Optional.of("array");
	}

	@Override
	public String getIdentifier() {
		return baseType.getPrefix().map(s -> s + " ").orElse("") + baseType.getIdentifier();
	}
}
