package byteback.mlcfg.syntax.types;

import byteback.mlcfg.identifiers.Identifier;

public class WhyReference implements WhyPtrType {
	private final Identifier.FQDN clazz;

	public WhyReference(Identifier.FQDN clazz) {
		this.clazz = clazz;
	}

	@Override
	public String getWhyAccessorScope() {
		return "L";
	}

	@Override
	public String getPreludeType() {
		return "Class %s.class".formatted(clazz);
	}
}
