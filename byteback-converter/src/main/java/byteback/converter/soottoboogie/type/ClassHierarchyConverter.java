package byteback.converter.soottoboogie.type;

import java.util.ArrayList;

import byteback.converter.soottoboogie.Prelude;
import byteback.frontend.boogie.ast.AxiomDeclaration;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.List;
import byteback.util.Lazy;
import soot.SootClass;

public class ClassHierarchyConverter {

	private static final Lazy<ClassHierarchyConverter> instance = Lazy.from(ClassHierarchyConverter::new);

	public static ClassHierarchyConverter v() {
		return instance.get();
	}

	public static AxiomDeclaration makeAxiom(final SootClass clazz, final SootClass superClazz) {
		final var axiomDeclaration = new AxiomDeclaration();
		final Expression bT1 = new TypeReferenceExtractor().visit(clazz.getType());
		final Expression bT2 = new TypeReferenceExtractor().visit(superClazz.getType());
		axiomDeclaration.setExpression(Prelude.v().makeExtendsExpression(bT1, bT2));

		return axiomDeclaration;
	}

	public List<AxiomDeclaration> convert(final SootClass clazz) {
		final var axioms = new List<AxiomDeclaration>();
		final var superClasses = new ArrayList<SootClass>();
		final SootClass superClass = clazz.getSuperclassUnsafe();

		if (superClass != null) {
			superClasses.add(clazz.getSuperclassUnsafe());
		}

		superClasses.addAll(clazz.getInterfaces());

		for (final SootClass superType : superClasses) {
			final AxiomDeclaration axiomDeclaration = makeAxiom(clazz, superType);
			axioms.add(axiomDeclaration);
		}

		return axioms;
	}

}
