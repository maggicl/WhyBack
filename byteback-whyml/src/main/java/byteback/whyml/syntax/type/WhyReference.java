package byteback.whyml.syntax.type;

import byteback.whyml.identifiers.Identifier;

public record WhyReference(Identifier.FQDN fqdn) implements WhyPtrType {
	@Override
	public String getWhyAccessorScope() {
		return "L";
	}

	@Override
	public String getPreludeType() {
		// we can't use the full name if we are in a scope that matches it
		return "Class %s.class".formatted(fqdn);
	}

	@Override
	public void accept(WhyTypeVisitor visitor) {
		visitor.visitReference(this);
	}
}
