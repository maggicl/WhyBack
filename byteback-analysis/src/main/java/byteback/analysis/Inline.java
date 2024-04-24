package byteback.analysis;

import byteback.analysis.util.SootHosts;
import soot.SootMethod;

public enum Inline {
	REQUIRED,
	OPTIONAL,
	NEVER;

	public boolean can() {
		return this != NEVER;
	}

	public boolean must() {
		return this == REQUIRED;
	}

	public static Inline parse(final SootMethod method) {
		// true if annotated with @Pure
		final boolean isPure = SootHosts.hasAnnotation(method, Namespace.PURE_ANNOTATION);

		// true if annotated with @Predicate
		final boolean isPredicate = SootHosts.hasAnnotation(method, Namespace.PREDICATE_ANNOTATION);

		// if @Predicate and @Pure, may be inlined
		// if only @Predicate, MUST be inlined (to make old(...) work)
		// if only @Pure, MUST NOT be inlined (as old(...) won't work in a declaration)
		return isPure && isPredicate
				? Inline.OPTIONAL
				: isPredicate
				? Inline.REQUIRED
				: Inline.NEVER;
	}
}