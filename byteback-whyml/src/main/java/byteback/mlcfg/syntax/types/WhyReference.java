package byteback.mlcfg.syntax.types;

import java.util.Optional;

// TODO: using references here is wrong. We need to implement a heap model
public class WhyReference implements WhyType {
	private final String className;

	public WhyReference(String className) {
		this.className = className;
	}

	@Override
	public Optional<String> getPrefix() {
		return Optional.of("ref");
	}

	@Override
	public String getIdentifier() {
		return className;
	}
}
