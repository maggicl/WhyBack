package byteback.whyml.syntax.type;

public interface WhyTypeVisitor {
	default void visitPrimitive(WhyJVMType t) {
	}

	default void visitReference(WhyReference t) {
	}

	default void visitUnit() {
	}

	default void visitArray(WhyArrayType t) {
		// recurse on element type of arrays
		t.baseType().accept(this);
	}
}
