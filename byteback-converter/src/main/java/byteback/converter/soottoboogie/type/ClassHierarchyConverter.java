package byteback.converter.soottoboogie.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Stack;

import byteback.analysis.RootResolver;
import byteback.converter.soottoboogie.Prelude;
import byteback.frontend.boogie.ast.AndOperation;
import byteback.frontend.boogie.ast.AxiomDeclaration;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.ImplicationOperation;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.NotEqualsOperation;
import byteback.frontend.boogie.ast.PartialOrderOperation;
import byteback.frontend.boogie.ast.UniversalQuantifier;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.builder.QuantifierExpressionBuilder;
import byteback.frontend.boogie.builder.SetBindingBuilder;
import byteback.util.Lazy;
import byteback.util.Stacks;
import soot.SootClass;

public class ClassHierarchyConverter {

	private static final Lazy<ClassHierarchyConverter> instance = Lazy.from(ClassHierarchyConverter::new);

	public static ClassHierarchyConverter v() {
		return instance.get();
	}

	public static String makeQuantifiedTypeVariableName(final int index) {
		return "t" + index;
	}

	public static Expression reduceConjunction(Stack<Expression> expressions) {
		if (expressions.size() > 1) {
			return Stacks.reduce(expressions, AndOperation::new);
		} else {
			return expressions.pop();
		}
	}

	public static Expression makeDisjointQuantifier(final Collection<SootClass> classes) {
		final Stack<Expression> antecedentStack = new Stack<>();
		final Stack<Expression> sequentStack = new Stack<>();
		final var quantifierBuilder = new QuantifierExpressionBuilder();
		quantifierBuilder.quantifier(new UniversalQuantifier());
		int i = 0;

		ValueReference previousParameterReference = null;

		for (final SootClass clazz : classes) {
			final String parameterName = makeQuantifiedTypeVariableName(i++);
			final var bindingBuilder = new SetBindingBuilder();
			bindingBuilder.typeAccess(Prelude.v().getTypeType().makeTypeAccess());
			bindingBuilder.name(parameterName);
			quantifierBuilder.addBinding(bindingBuilder.build());

			final ValueReference parameterReference = ValueReference.of(parameterName);
			final ValueReference typeReference = ValueReference.of(ReferenceTypeConverter.typeName(clazz));
			final Expression extendsReference = new PartialOrderOperation(parameterReference, typeReference);
			antecedentStack.push(extendsReference);
			quantifierBuilder.addTrigger(extendsReference);

			if (previousParameterReference != null) {
				Expression left = previousParameterReference;
				Expression right = parameterReference;
				sequentStack.push(new NotEqualsOperation(left, right));
			}

			previousParameterReference = parameterReference;
		}

		final Expression antecedent = reduceConjunction(antecedentStack);
		final Expression sequent = reduceConjunction(sequentStack);

		quantifierBuilder.operand(new ImplicationOperation(antecedent, sequent));

		return quantifierBuilder.build();
	}

	public static Optional<AxiomDeclaration> makeDisjointAxiom(final SootClass clazz, final RootResolver resolver) {
		final Collection<SootClass> subclasses = resolver.getVisibleSubclassesOf(clazz);

		if (subclasses.size() > 1) {
			final var axiomDeclaration = new AxiomDeclaration();
			axiomDeclaration.setExpression(makeDisjointQuantifier(subclasses));
			return Optional.of(axiomDeclaration);
		}

		return Optional.empty();
	}

	public static AxiomDeclaration makeExtendsAxiom(final SootClass clazz, final SootClass superClazz) {
		final var axiomDeclaration = new AxiomDeclaration();
		final Expression bT1 = new TypeReferenceExtractor().visit(clazz.getType());
		final Expression bT2 = new TypeReferenceExtractor().visit(superClazz.getType());
		axiomDeclaration.setExpression(new PartialOrderOperation(bT1, bT2));

		return axiomDeclaration;
	}

	public List<AxiomDeclaration> convert(final SootClass clazz, final RootResolver resolver) {
		final var axioms = new List<AxiomDeclaration>();
		final var superClasses = new ArrayList<SootClass>();
		final SootClass superClass = clazz.getSuperclassUnsafe();

		if (superClass != null) {
			superClasses.add(clazz.getSuperclassUnsafe());
		}

		superClasses.addAll(clazz.getInterfaces());

		for (final SootClass superType : superClasses) {
			axioms.add(makeExtendsAxiom(clazz, superType));
		}

		makeDisjointAxiom(clazz, resolver).ifPresent(axioms::add);

		return axioms;
	}

}
