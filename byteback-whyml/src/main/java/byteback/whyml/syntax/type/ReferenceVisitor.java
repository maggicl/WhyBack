package byteback.whyml.syntax.type;

import java.util.Optional;

public class ReferenceVisitor implements WhyTypeVisitor {
	private WhyReference ref = null;

	@Override
	public void visitReference(WhyReference t) {
		ref = t;
	}

	public Optional<WhyReference> getReference() {
		return Optional.ofNullable(ref);
	}

	public static Optional<WhyReference> get(WhyType t) {
		final ReferenceVisitor e = new ReferenceVisitor();
		t.accept(e);
		return e.getReference();
	}
}
