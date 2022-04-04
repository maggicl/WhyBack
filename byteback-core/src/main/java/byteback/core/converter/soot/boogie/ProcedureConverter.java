package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.annotation.SootAnnotationElement.StringElementExtractor;
import byteback.core.representation.soot.body.SootBody;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.BoundedBinding;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.PostCondition;
import byteback.frontend.boogie.ast.PreCondition;
import byteback.frontend.boogie.ast.ProcedureDeclaration;
import byteback.frontend.boogie.ast.ProcedureSignature;
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

public class ProcedureConverter {

	private static final ProcedureConverter instance = new ProcedureConverter();

	public static ProcedureConverter instance() {
		return instance;
	}

	public static BoundedBinding makeBinding(final Local local) {
		final SootType type = new SootType(local.getType());
		final TypeAccess typeAccess = new TypeAccessExtractor().visit(type);
		final BoundedBindingBuilder bindingBuilder = new BoundedBindingBuilder();
		bindingBuilder.addName(local.getName()).typeAccess(typeAccess);

		return bindingBuilder.build();
	}

	public static ProcedureSignature makeSignature(final SootMethod method) {
		final ProcedureSignatureBuilder signatureBuilder = new ProcedureSignatureBuilder();

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
				final BoundedBinding binding = Prelude.getReturnBindingBuilder().typeAccess(typeAccess).build();
				signatureBuilder.addOutputBinding(binding);
			}

		});

		return signatureBuilder.build();
	}

	public static Body makeBody(final SootMethod method) {
		final SootType returnType = method.getReturnType();
		final SootBody body = method.getBody();
		final Body boogieBody = new Body();
		final LabelCollector labelCollector = new LabelCollector();
		final LoopCollector loopCollector = new LoopCollector();
		labelCollector.collect(body);
		loopCollector.collect(body);
		body.apply(new ProcedureBodyExtractor(boogieBody, returnType, labelCollector, loopCollector));

		for (Local local : method.getBody().getLocals()) {
			final VariableDeclarationBuilder variableBuilder = new VariableDeclarationBuilder();
			boogieBody.addLocalDeclaration(variableBuilder.addBinding(makeBinding(local)).build());
		}

		return boogieBody;
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
		parameterTypes.add(target.getReturnType());
		final SootType returnType = new SootType(BooleanType.v());
		final SootMethod source = target.getSootClass().getSootMethod(sourceName, parameterTypes, returnType)
				.orElseThrow(() -> new IllegalArgumentException("Could not find condition method " + sourceName));

		return FunctionManager.instance().convert(source).getFunction().inline(arguments);
	}

	public static List<Expression> makeParameters(final SootMethod method) {
		final List<Expression> references = new List<>(Prelude.getHeapVariable().getValueReference());

		for (Local local : method.getBody().getParameterLocals()) {
			references.add(new ValueReference(new Accessor(local.getName())));
		}

		references.add(Prelude.getReturnValueReference());

		return references;
	}

	public static List<Specification> makeSpecifications(final SootMethod method) {
		final Stream<Specification> preconditions = makeConditions(method, Annotations.REQUIRE_ANNOTATION)
				.map((expression) -> new PreCondition(false, expression));
		final Stream<Specification> postconditions = makeConditions(method, Annotations.ENSURE_ANNOTATION)
				.map((expression) -> new PostCondition(false, expression));

		return new List<Specification>().addAll(Stream.concat(preconditions, postconditions)::iterator);
	}

	public ProcedureDeclaration convert(final SootMethod method) {
		final ProcedureDeclarationBuilder procedureBuilder = new ProcedureDeclarationBuilder();
		final ProcedureSignature signature = makeSignature(method);
		final List<Specification> specifications = makeSpecifications(method);
		final Body body = makeBody(method);
		procedureBuilder.name(NameConverter.methodName(method));
		procedureBuilder.signature(signature);
		procedureBuilder.body(body);
		procedureBuilder.specification(specifications);

		return procedureBuilder.build();
	}

}
