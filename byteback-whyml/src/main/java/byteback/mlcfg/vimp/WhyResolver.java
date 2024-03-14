package byteback.mlcfg.vimp;

import byteback.mlcfg.syntax.WhyClass;
import byteback.mlcfg.syntax.types.WhyReference;
import byteback.mlcfg.syntax.types.WhyType;
import byteback.mlcfg.syntax.types.WhyTypeVisitor;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class WhyResolver {
	private final Set<WhyClass> classes = new HashSet<>();
	private final Set<WhyReference> resolvedRefTypes = new HashSet<>();

	public void add(final WhyClass classDeclaration) {
		classes.add(classDeclaration);
		resolvedRefTypes.add(classDeclaration.type());
	}

	public boolean isResolved(WhyType t) {
		final ResolvedVisitor r = new ResolvedVisitor();
		t.accept(r);
		return r.resolved;
	}

	public Stream<WhyClass> stream() {
		return classes.stream();
	}

	private class ResolvedVisitor implements WhyTypeVisitor {
		private boolean resolved = true;

		@Override
		public void visitReference(WhyReference t) {
			resolved = resolved && resolvedRefTypes.contains(t);
		}
	}
}
