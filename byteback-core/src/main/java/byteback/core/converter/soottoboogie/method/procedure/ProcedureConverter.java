package byteback.core.converter.soottoboogie.method.procedure;

import byteback.core.converter.soottoboogie.Convention;
import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.converter.soottoboogie.Namespace;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.expression.ExpressionExtractor;
import byteback.core.converter.soottoboogie.method.MethodConverter;
import byteback.core.converter.soottoboogie.method.function.FunctionManager;
import byteback.core.converter.soottoboogie.type.TypeAccessExtractor;
import byteback.core.converter.soottoboogie.type.TypeReferenceExtractor;
import byteback.core.representation.soot.annotation.SootAnnotations;
import byteback.core.representation.soot.annotation.SootAnnotationElems.StringElemExtractor;
import byteback.core.representation.soot.body.SootBodies;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.core.representation.soot.unit.SootMethods;
import byteback.frontend.boogie.ast.Assignee;
import byteback.frontend.boogie.ast.AssignmentStatement;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.BoundedBinding;
import byteback.frontend.boogie.ast.Condition;
import byteback.frontend.boogie.ast.EqualsOperation;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.ExtensionPoint;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.PostCondition;
import byteback.frontend.boogie.ast.PreCondition;
import byteback.frontend.boogie.ast.FrameCondition;
import byteback.frontend.boogie.ast.ProcedureDeclaration;
import byteback.frontend.boogie.ast.SymbolicReference;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.builder.BoundedBindingBuilder;
import byteback.frontend.boogie.builder.ProcedureDeclarationBuilder;
import byteback.frontend.boogie.builder.ProcedureSignatureBuilder;
import byteback.frontend.boogie.builder.VariableDeclarationBuilder;

import java.util.ArrayList;
import java.util.function.Supplier;
import soot.BooleanType;
import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.VoidType;
import soot.tagkit.AnnotationElem;

public class ProcedureConverter extends MethodConverter {

	public static final String PARAMETER_PREFIX = "?";

	private static final ProcedureConverter instance = new ProcedureConverter();

	public static ProcedureConverter v() {
		return instance;
	}

	public static String parameterName(final Local local) {
		return PARAMETER_PREFIX + local.getName();
	}

	public static BoundedBinding makeBinding(final String name, final Type type) {
		final var bindingBuilder = new BoundedBindingBuilder();
		final SymbolicReference typeReference = new TypeReferenceExtractor().visit(type);
		final TypeAccess typeAccess = new TypeAccessExtractor().visit(type);
		bindingBuilder.addName(name).typeAccess(typeAccess);

		if (typeReference != null) {
			final FunctionReference typeOfReference = Prelude.v().getTypeOfFunction()
					.makeFunctionReference();
			final ValueReference heapReference = Prelude.v().getHeapVariable()
					.makeValueReference();
			typeOfReference.addArgument(heapReference);
			typeOfReference.addArgument(ValueReference.of(name));
			bindingBuilder.whereClause(new EqualsOperation(typeOfReference, typeReference));
		}

		return bindingBuilder.build();
	}

	public static BoundedBinding makeBinding(final Local local, final String name) {
		final Type type = local.getType();

		return makeBinding(name, type);
	}

	public static BoundedBinding makeBinding(final Local local) {
		final String name = ExpressionExtractor.localName(local);

		return makeBinding(local, name);
	}

	public static Iterable<Local> getParameterLocals(final SootMethod method) {
		if (SootMethods.hasBody(method)) {
			return SootBodies.getParameterLocals(method.retrieveActiveBody());
		} else {
			return SootMethods.makeFakeParameterLocals(method);
		}
	}

	public static void buildSignature(final ProcedureDeclarationBuilder builder, final SootMethod method) {
		final var signatureBuilder = new ProcedureSignatureBuilder();

		for (Local local : getParameterLocals(method)) {
			final String parameterName = parameterName(local);
			signatureBuilder.addInputBinding(makeBinding(local, parameterName));
		}

		method.getReturnType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseVoidType(final VoidType type) {
				// Do not add output parameter
			}

			@Override
			public void caseDefault(final Type type) {
				final TypeAccess typeAccess = new TypeAccessExtractor().visit(method.getReturnType());
				final BoundedBinding binding = Convention.makeReturnBinding(typeAccess);
				signatureBuilder.addOutputBinding(binding);
			}

		});

		builder.signature(signatureBuilder.build());
	}

	public static List<Expression> makeArguments(final SootMethod method) {
		final List<Expression> references = new List<>();

		references.add(Prelude.v().getHeapVariable().makeValueReference());

		for (Local local : getParameterLocals(method)) {
			references.add(ValueReference.of(parameterName(local)));
		}

		references.add(Convention.makeReturnReference());

		return references;
	}

	public static Expression makeCondition(final SootMethod target, final SootMethod source) {
		final List<Expression> arguments = makeArguments(target);

		if (source.isStatic() != target.isStatic()) {
			throw new ConversionException("Incompatible target type for condition method " + source.getName());
		}

		return FunctionManager.instance().convert(source).getFunction().inline(arguments);
	}

	public static void buildSpecification(final ProcedureDeclarationBuilder builder, final SootMethod method) {

		SootMethods.getAnnotations(method).forEach((tag) -> {
			SootAnnotations.getAnnotations(tag).forEach((sub) -> {
				final ArrayList<Type> parameters = new ArrayList<>(method.getParameterTypes());
				final Supplier<Condition> conditionSupplier;

				switch (sub.getType()) {
					case Namespace.REQUIRE_ANNOTATION:
						conditionSupplier = PreCondition::new;
						break;
					case Namespace.ENSURE_ANNOTATION:
						conditionSupplier = PostCondition::new;
						new SootTypeVisitor<>() {

							@Override
							public void caseVoidType(final VoidType type) {
							}

							@Override
							public void caseDefault(final Type type) {
								parameters.add(type);
							}

						}.visit(method.getReturnType());
						break;
					default:
						return;
				}

				final AnnotationElem elem = SootAnnotations.getValue(sub).orElseThrow();
				final String name = new StringElemExtractor().visit(elem);
				final SootClass clazz = method.getDeclaringClass();
				final SootMethod source = clazz.getMethodUnsafe(name, parameters, BooleanType.v());

				if (source == null) {
					throw new ConversionException("Unable to find matching predicate " + name + " in class" + clazz.getName());
				}

				final Expression expression = makeCondition(method, source);
				final Condition condition = conditionSupplier.get();
				condition.setFree(false);
				condition.setExpression(expression);
				builder.addSpecification(condition);
			});
		});
	}

	public static void buildBody(final ProcedureDeclarationBuilder builder, final SootMethod method) {
		final var bodyExtractor = new ProcedureBodyExtractor(method.getReturnType());
		final Body body = bodyExtractor.visit(method.retrieveActiveBody());

		for (Local local : SootBodies.getLocals(method.retrieveActiveBody())) {
			final var variableBuilder = new VariableDeclarationBuilder();
			body.addLocalDeclaration(variableBuilder.addBinding(makeBinding(local)).build());
		}

		final var assignments = new ExtensionPoint();
		body.getStatementList().insertChild(assignments, 0);
		
		for (Local local : SootBodies.getParameterLocals(method.retrieveActiveBody())) {
			final var variableBuilder = new VariableDeclarationBuilder();
			final var assignment = new AssignmentStatement(Assignee.of(ValueReference.of(ExpressionExtractor.localName(local))), ValueReference.of(parameterName(local)));
			body.addLocalDeclaration(variableBuilder.addBinding(makeBinding(local)).build());
			assignments.addStatement(assignment);
		}

		builder.body(body);
	}

	public static void buildFrameInvariant(final ProcedureDeclarationBuilder builder) {
		builder.addSpecification(Prelude.v().makeHeapFrameCondition());
	}

	public ProcedureDeclaration convert(final SootMethod method) {
		final var builder = new ProcedureDeclarationBuilder();

		try {
			builder.name(methodName(method));
			buildSignature(builder, method);
			buildSpecification(builder, method);

			if (SootMethods.hasBody(method)) {
				if(!SootMethods.hasAnnotation(method, Namespace.LEMMA_ANNOTATION)) {
					buildBody(builder, method);
				}
			} else {
				buildFrameInvariant(builder);
			}
		} catch (final ConversionException exception) {
			throw new ProcedureConversionException(method, exception);
		}

		return builder.build();
	}

}
