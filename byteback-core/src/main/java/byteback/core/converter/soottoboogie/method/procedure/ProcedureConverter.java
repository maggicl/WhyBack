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
import byteback.core.representation.soot.annotation.SootAnnotationElement;
import byteback.core.representation.soot.annotation.SootAnnotationElement.StringElementExtractor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.BoundedBinding;
import byteback.frontend.boogie.ast.Condition;
import byteback.frontend.boogie.ast.EqualsOperation;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionReference;
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
import java.util.Collection;
import java.util.function.Supplier;
import soot.Local;
import soot.Type;
import soot.VoidType;

public class ProcedureConverter extends MethodConverter {

	private static final ProcedureConverter instance = new ProcedureConverter();

	public static ProcedureConverter instance() {
		return instance;
	}

	public static BoundedBinding makeBinding(final Local local) {
		final var type = new SootType(local.getType());
		final var bindingBuilder = new BoundedBindingBuilder();
		final SymbolicReference typeReference = new TypeReferenceExtractor().visit(type);
		final TypeAccess typeAccess = new TypeAccessExtractor().visit(type);
		bindingBuilder.addName(ExpressionExtractor.localName(local)).typeAccess(typeAccess);

		if (typeReference != null) {
			final FunctionReference typeOfReference = Prelude.instance().getTypeOfFunction().makeFunctionReference();
			final ValueReference heapReference = Prelude.instance().getHeapVariable().makeValueReference();
			typeOfReference.addArgument(heapReference);
			typeOfReference.addArgument(ValueReference.of(ExpressionExtractor.localName(local)));
			bindingBuilder.whereClause(new EqualsOperation(typeOfReference, typeReference));
		}

		return bindingBuilder.build();
	}

	public static void buildSignature(final ProcedureDeclarationBuilder builder, final SootMethod method) {
		final var signatureBuilder = new ProcedureSignatureBuilder();

		for (Local local : method.getBody().getParameterLocals()) {
			signatureBuilder.addInputBinding(makeBinding(local));
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

		references.add(Prelude.instance().getHeapVariable().makeValueReference());

		for (Local local : method.getBody().getParameterLocals()) {
			references.add(ValueReference.of(ExpressionExtractor.localName(local)));
		}

		references.add(Convention.makeReturnReference());

		return references;
	}

	public static Expression makeCondition(final SootMethod target, final String sourceName,
			final Collection<SootType> sourceParameters) {
		final List<Expression> arguments = makeArguments(target);
		final SootMethod source = target.getSootClass()
				.getSootMethod(sourceName, sourceParameters, SootType.booleanType())
				.orElseThrow(() -> new ConversionException("Could not find condition method " + sourceName
						+ " matching with the target method's signature " + target.getIdentifier()));

		if (source.isStatic() != target.isStatic()) {
			throw new ConversionException("Incompatible target type for condition method " + sourceName);
		}

		return FunctionManager.instance().convert(source).getFunction().inline(arguments);
	}

	public static void buildSpecifications(final ProcedureDeclarationBuilder builder, final SootMethod method) {
		final Collection<SootType> sourceParameters = method.getParameterTypes();

		method.annotations().forEach((annotation) -> {
			final Supplier<Condition> supplier;

			switch (annotation.getTypeName()) {
				case Namespace.REQUIRE_ANNOTATION :
				case Namespace.REQUIRES_ANNOTATION :
					supplier = PreCondition::new;
					break;

				case Namespace.ENSURE_ANNOTATION :
				case Namespace.ENSURES_ANNOTATION :
					final SootType returnType = method.getReturnType();
					returnType.apply(new SootTypeVisitor<>() {

						@Override
						public void caseVoidType(final VoidType voidType) {
							// Source does not include a return parameter
						}

						@Override
						public void caseDefault(final Type type) {
							sourceParameters.add(returnType);
						}

					});
					supplier = PostCondition::new;
					break;

				default :
					return;
			}

			final SootAnnotationElement element = annotation.getValue().orElseThrow(() -> new ConversionException(
					"Annotation " + annotation.getTypeName() + " requires a value argument"));

			element.flatten().forEach((value) -> {
				final String sourceName = new StringElementExtractor().visit(value);
				final Expression expression = makeCondition(method, sourceName, sourceParameters);
				final Condition condition = supplier.get();
				condition.setFree(false);
				condition.setExpression(expression);
				builder.addSpecification(condition);
			});
		});
	}

	public static void buildBody(final ProcedureDeclarationBuilder builder, final SootMethod method) {
		final SootType returnType = method.getReturnType();
		final var bodyExtractor = new ProcedureBodyExtractor(returnType);
		final Body body = bodyExtractor.visit(method.getBody());

		for (Local local : method.getBody().getLocals()) {
			final var variableBuilder = new VariableDeclarationBuilder();
			body.addLocalDeclaration(variableBuilder.addBinding(makeBinding(local)).build());
		}

		builder.body(body);
	}

	public ProcedureDeclaration convert(final SootMethod method) {
		final var builder = new ProcedureDeclarationBuilder();

		try {
			builder.name(methodName(method));
			buildSignature(builder, method);
			buildSpecifications(builder, method);

			if (method.hasBody() && method.annotation(Namespace.LEMMA_ANNOTATION).isEmpty()) {
				buildBody(builder, method);
			}
		} catch (ConversionException exception) {
			throw new ProcedureConversionException(method, exception);
		}

		final ProcedureDeclaration declaration = builder.build().rootedCopy();
		declaration.removeUnusedVariables();

		return declaration;
	}

}
