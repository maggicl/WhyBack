package byteback.converter.soottoboogie.method.procedure;

import byteback.analysis.Namespace;
import byteback.analysis.util.SootAnnotationElems.StringElemExtractor;
import byteback.analysis.util.SootAnnotationElems.ClassElemExtractor;
import byteback.analysis.util.SootAnnotations;
import byteback.analysis.util.SootBodies;
import byteback.analysis.util.SootMethods;
import byteback.converter.soottoboogie.Convention;
import byteback.converter.soottoboogie.ConversionException;
import byteback.converter.soottoboogie.Prelude;
import byteback.converter.soottoboogie.expression.PureExpressionExtractor;
import byteback.converter.soottoboogie.method.MethodConverter;
import byteback.converter.soottoboogie.method.function.FunctionManager;
import byteback.converter.soottoboogie.type.TypeAccessExtractor;
import byteback.converter.soottoboogie.type.TypeReferenceExtractor;
import byteback.frontend.boogie.ast.Assignee;
import byteback.frontend.boogie.ast.AssignmentStatement;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.BoundedBinding;
import byteback.frontend.boogie.ast.Condition;
import byteback.frontend.boogie.ast.EqualsOperation;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.ImplicationOperation;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.PostCondition;
import byteback.frontend.boogie.ast.PreCondition;
import byteback.frontend.boogie.ast.ProcedureDeclaration;
import byteback.frontend.boogie.ast.SymbolicReference;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.builder.BoundedBindingBuilder;
import byteback.frontend.boogie.builder.ProcedureDeclarationBuilder;
import byteback.frontend.boogie.builder.ProcedureSignatureBuilder;
import byteback.frontend.boogie.builder.VariableDeclarationBuilder;
import byteback.util.Lazy;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import soot.BooleanType;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.VoidType;
import soot.tagkit.AnnotationElem;

public class ProcedureConverter extends MethodConverter {

	public static final String PARAMETER_PREFIX = "?";

	private static final Lazy<ProcedureConverter> instance = Lazy.from(ProcedureConverter::new);

	public static ProcedureConverter v() {
		return instance.get();
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
			final FunctionReference instanceOfReference = Prelude.v().getInstanceOfFunction().makeFunctionReference();
			final ValueReference heapReference = Prelude.v().getHeapVariable().makeValueReference();
			instanceOfReference.addArgument(heapReference);
			instanceOfReference.addArgument(ValueReference.of(name));
			instanceOfReference.addArgument(typeReference);
			bindingBuilder.whereClause(instanceOfReference);
		}

		return bindingBuilder.build();
	}

	public static BoundedBinding makeBinding(final Local local, final String name) {
		final Type type = local.getType();

		return makeBinding(name, type);
	}

	public static BoundedBinding makeBinding(final Local local) {
		final String name = PureExpressionExtractor.localName(local);

		return makeBinding(local, name);
	}

	public static Iterable<Local> getParameterLocals(final SootMethod method) {
		if (SootMethods.hasBody(method)) {
			return SootBodies.getParameterLocals(method.retrieveActiveBody());
		} else {
			return SootMethods.makeFakeParameterLocals(method);
		}
	}

	public static void buildReturnParameter(final ProcedureSignatureBuilder builder, final SootMethod method) {
		final TypeAccess typeAccess = new TypeAccessExtractor().visit(method.getReturnType());
		final BoundedBinding binding = Convention.makeReturnBinding(typeAccess);
		builder.addOutputBinding(binding);
	}

	public static void buildExceptionParameter(final ProcedureSignatureBuilder builder, final SootMethod method) {
		final TypeAccess typeAccess = new TypeAccessExtractor().visit(RefType.v());
		final BoundedBinding binding = Convention.makeExceptionBinding(typeAccess);
		builder.addOutputBinding(binding);
	}

	public static void buildSignature(final ProcedureDeclarationBuilder builder, final SootMethod method) {
		final var signatureBuilder = new ProcedureSignatureBuilder();

		for (Local local : getParameterLocals(method)) {
			final String parameterName = parameterName(local);
			signatureBuilder.addInputBinding(makeBinding(local, parameterName));
		}

		if (method.getReturnType() != VoidType.v()) {
			buildReturnParameter(signatureBuilder, method);
		}

		buildExceptionParameter(signatureBuilder, method);
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

		return FunctionManager.v().convert(source).getFunction().inline(arguments);
	}

	public static void buildSpecification(final ProcedureDeclarationBuilder builder, final SootMethod method) {
		var checksExceptions = new AtomicBoolean(false);

		SootMethods.getAnnotations(method).forEach((tag) -> {
			SootAnnotations.getAnnotations(tag).forEach((sub) -> {
				final ArrayList<Type> parameters = new ArrayList<>(method.getParameterTypes());
				final Function<Expression, Condition> conditionCtor;

				switch (sub.getType()) {
					case Namespace.REQUIRE_ANNOTATION:
						conditionCtor = (expression) -> new PreCondition(false, expression);
						break;
					case Namespace.ENSURE_ANNOTATION:
						conditionCtor = (expression) -> new PostCondition(false, expression);

						if (method.getReturnType() != VoidType.v()) {
								parameters.add(method.getReturnType());
						}

						break;
					case Namespace.RAISE_ANNOTATION:
						final AnnotationElem exceptionElem = SootAnnotations.getElem(sub, "exception").orElseThrow();
						final String value = new ClassElemExtractor().visit(exceptionElem);
						final RefType exceptionType = Scene.v().loadClass(Namespace.stripDescriptor(value), 0).getType();
						final SymbolicReference typeReference = new TypeReferenceExtractor().visit(exceptionType);
						final FunctionReference instanceOfReference = Prelude.v().getInstanceOfFunction().makeFunctionReference();
						final ValueReference heapReference = Prelude.v().getHeapVariable().makeValueReference();
						instanceOfReference.addArgument(heapReference);
						instanceOfReference.addArgument(Convention.makeExceptionReference());
						instanceOfReference.addArgument(typeReference);
						conditionCtor = (expression) -> new PostCondition(false, new ImplicationOperation(expression, instanceOfReference));
						checksExceptions.set(true);
						break;
					default:
						return;
				}

				final AnnotationElem elem = SootAnnotations.getValue(sub).orElseThrow();
				final String name = new StringElemExtractor().visit(elem);
				final SootClass clazz = method.getDeclaringClass();
				final SootMethod source = clazz.getMethodUnsafe(name, parameters, BooleanType.v());

				if (source == null) {
					throw new ConversionException(
							"Unable to find matching predicate " + name + " in class" + clazz.getName());
				}

				final Expression expression = makeCondition(method, source);
				final Condition condition = conditionCtor.apply(expression);
				builder.addSpecification(condition);
			});
		});

		if (!checksExceptions.get()) {
			final PostCondition exceptionalCondition = new PostCondition();
			final EqualsOperation expression = new EqualsOperation(Convention.makeExceptionReference(),
					Prelude.v().getNullConstant().makeValueReference());
			exceptionalCondition.setExpression(expression);
			builder.addSpecification(exceptionalCondition);
		}
	}

	public static void insertExceptionInitialization(final ProcedureBodyExtractor bodyExtractor) {
		final Assignee assignee = Assignee.of(Convention.makeExceptionReference());
		final ValueReference assigned = Prelude.v().getNullConstant().makeValueReference();
		final var assignment = new AssignmentStatement(assignee, assigned);
		bodyExtractor.addStatement(assignment);
	}

	public static void buildBody(final ProcedureDeclarationBuilder builder, final SootMethod method) {
		final var bodyExtractor = new ProcedureBodyExtractor();
		insertExceptionInitialization(bodyExtractor);
		final Body body = bodyExtractor.visit(method.retrieveActiveBody());

		for (Local local : SootBodies.getLocals(method.retrieveActiveBody())) {
			final var variableBuilder = new VariableDeclarationBuilder();
			body.addLocalDeclaration(variableBuilder.addBinding(makeBinding(local)).build());
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
				if (!SootMethods.hasAnnotation(method, Namespace.LEMMA_ANNOTATION)) {
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
