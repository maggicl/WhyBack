package byteback.core.converter.soottoboogie.method.procedure;

import byteback.core.converter.soottoboogie.AnnotationContext;
import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.method.MethodConverter;
import byteback.core.converter.soottoboogie.method.function.FunctionManager;
import byteback.core.converter.soottoboogie.type.TypeAccessExtractor;
import byteback.core.representation.soot.annotation.SootAnnotationElement.StringElementExtractor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.BoundedBinding;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.PostCondition;
import byteback.frontend.boogie.ast.PreCondition;
import byteback.frontend.boogie.ast.ProcedureDeclaration;
import byteback.frontend.boogie.ast.Specification;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.builder.BoundedBindingBuilder;
import byteback.frontend.boogie.builder.ProcedureDeclarationBuilder;
import byteback.frontend.boogie.builder.ProcedureSignatureBuilder;
import byteback.frontend.boogie.builder.VariableDeclarationBuilder;
import java.util.Collection;
import java.util.stream.Stream;
import soot.BooleanType;
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
		final TypeAccess typeAccess = new TypeAccessExtractor().visit(type);
		bindingBuilder.addName(local.getName()).typeAccess(typeAccess);

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
				final BoundedBinding binding = Prelude.getReturnBinding(typeAccess);
				signatureBuilder.addOutputBinding(binding);
			}

		});

		builder.signature(signatureBuilder.build());
	}

	public static Stream<Expression> makeConditions(final SootMethod method, final String typeName) {
		final Stream<String> sourceNames = method.getAnnotationValues(typeName)
				.map((element) -> new StringElementExtractor().visit(element));
		final List<Expression> arguments = makeParameters(method);

		return sourceNames.map((sourceName) -> makeCondition(method, sourceName, arguments));
	}

	public static Expression makeCondition(final SootMethod target, final String sourceName,
			final List<Expression> arguments) {
		final Collection<SootType> parameterTypes = target.getParameterTypes();
		final SootType returnType = target.getReturnType();
		returnType.apply(new SootTypeVisitor<>() {

			@Override
			public void caseVoidType(final VoidType voidType) {
				// Source will not include return parameter
			}

			@Override
			public void caseDefault(final Type type) {
				parameterTypes.add(returnType);
			}

		});
		final SootMethod source = target.getSootClass()
				.getSootMethod(sourceName, parameterTypes, new SootType(BooleanType.v()))
				.orElseThrow(() -> new ConversionException("Could not find condition method " + sourceName));

		if (source.isStatic() != target.isStatic()) {
			throw new ConversionException("Incompatible target type for condition method " + sourceName);
		}

		return FunctionManager.instance().convert(source).getFunction().inline(arguments);
	}

	public static List<Expression> makeParameters(final SootMethod method) {
		final List<Expression> references = new List<>(Prelude.getHeapVariable().makeValueReference());

		for (Local local : method.getBody().getParameterLocals()) {
			references.add(ValueReference.of(local.getName()));
		}

		references.add(Prelude.getReturnValueReference());

		return references;
	}

	public static void buildSpecifications(final ProcedureDeclarationBuilder builder, final SootMethod method) {
		final Stream<Specification> preconditions = makeConditions(method, AnnotationContext.REQUIRE_ANNOTATION)
				.map((expression) -> new PreCondition(false, expression));
		final Stream<Specification> postconditions = makeConditions(method, AnnotationContext.ENSURE_ANNOTATION)
				.map((expression) -> new PostCondition(false, expression));

		builder.specification(preconditions::iterator);
		builder.specification(postconditions::iterator);
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
		final var procedureBuilder = new ProcedureDeclarationBuilder();

		try {
			procedureBuilder.name(methodName(method));
			buildSignature(procedureBuilder, method);
			buildSpecifications(procedureBuilder, method);
			buildBody(procedureBuilder, method);
		} catch (ConversionException exception) {
			throw new ProcedureConversionException(method, exception);
		}

		final ProcedureDeclaration declaration = procedureBuilder.build().rootedCopy();
		declaration.removeUnusedVariables();

		return declaration;
	}

}
