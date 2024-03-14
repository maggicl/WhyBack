package byteback.mlcfg.syntax.types;

import byteback.mlcfg.identifiers.Identifier;
import java.util.Objects;

public class WhyReference implements WhyPtrType {
	public Identifier.FQDN fqdn() {
		return clazz;
	}

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
		// we can't use the full name if we are in a scope that matches it
		return "Class %s.class".formatted(clazz);
	}

	@Override
	public void accept(WhyTypeVisitor visitor) {
		visitor.visitReference(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WhyReference that = (WhyReference) o;
		return Objects.equals(clazz, that.clazz);
	}

	@Override
	public int hashCode() {
		return Objects.hash(clazz);
	}
}
