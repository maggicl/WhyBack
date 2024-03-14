package byteback.mlcfg.syntax.types;

public interface WhyTypeVisitor {
	default void visitPrimitive(WhyPrimitive t) {
	}

	default void visitReference(WhyReference t) {
	}

	default void visitArray(WhyArrayType t) {
		// recurse on element type of arrays
		t.getBaseType().accept(this);
	}
}
