package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.annotation.SootAnnotationElement.StringElementExtractor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.core.representation.soot.unit.SootMethodUnit;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.BoundedBinding;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.Label;
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
import java.util.Map;
import java.util.stream.Stream;
import soot.BooleanType;
import soot.Local;
import soot.Type;
import soot.Unit;
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

	public static ProcedureSignature makeSignature(final SootMethodUnit methodUnit) {
		final ProcedureSignatureBuilder signatureBuilder = new ProcedureSignatureBuilder();

		for (Local local : methodUnit.getBody().getParameterLocals()) {
			signatureBuilder.addInputBinding(makeBinding(local));
		}

		methodUnit.getReturnType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseVoidType(final VoidType type) {
				// Do not add output parameter
			}

			@Override
			public void caseDefault(final Type type) {
				final TypeAccess typeAccess = new TypeAccessExtractor().visit(methodUnit.getReturnType());
				final BoundedBinding binding = Prelude.getReturnBindingBuilder().typeAccess(typeAccess).build();
				signatureBuilder.addOutputBinding(binding);
			}

		});

		return signatureBuilder.build();
	}

	public static Body makeBody(final SootMethodUnit methodUnit) {
		final Body body = new Body();
		final Map<Unit, Label> labelTable = new LabelCollector().visit(methodUnit.getBody());
		methodUnit.getBody().apply(new ProcedureBodyExtractor(body, methodUnit.getReturnType(), labelTable));

		for (Local local : methodUnit.getBody().getLocals()) {
			final VariableDeclarationBuilder variableBuilder = new VariableDeclarationBuilder();
			body.addLocalDeclaration(variableBuilder.addBinding(makeBinding(local)).build());
		}

		return body;
	}

	public static Stream<Expression> makeConditions(final SootMethodUnit methodUnit, final String annotation) {
		final Stream<String> sourceNames = methodUnit.getAnnotationValues(annotation)
				.map((element) -> new StringElementExtractor().visit(element));
		final List<Expression> arguments = makeArguments(methodUnit);

		return sourceNames.map((sourceName) -> makeCondition(methodUnit, sourceName, arguments));
	}

	public static Expression makeCondition(final SootMethodUnit target, final String sourceName,
			final List<Expression> arguments) {

		final Collection<SootType> parameterTypes = target.getParameterTypes();
		parameterTypes.add(target.getReturnType());
		final SootType returnType = new SootType(BooleanType.v());
		final SootMethodUnit source = target.getClassUnit().getMethodUnit(sourceName, parameterTypes, returnType)
				.orElseThrow(() -> new IllegalArgumentException("Could not find condition method " + sourceName));

		return ConditionManager.instance().convert(source).getFunction().inline(arguments);
	}

	public static List<Expression> makeArguments(final SootMethodUnit methodUnit) {
		final List<Expression> references = new List<>(Prelude.getHeapVariable().getValueReference());

		for (Local local : methodUnit.getBody().getParameterLocals()) {
			references.add(new ValueReference(new Accessor(local.getName())));
		}

		references.add(Prelude.getReturnValueReference());

		return references;
	}

	public static List<Specification> makeSpecifications(final SootMethodUnit methodUnit) {
		final Stream<Specification> preconditions = makeConditions(methodUnit,
				"Lbyteback/annotations/Contract$Require;").map((expression) -> new PreCondition(false, expression));
		final Stream<Specification> postconditions = makeConditions(methodUnit,
				"Lbyteback/annotations/Contract$Ensure;").map((expression) -> new PostCondition(false, expression));
		final Iterable<Specification> specifications = Stream.concat(preconditions, postconditions)::iterator;

		return new List<Specification>().addAll(specifications);
	}

	public ProcedureDeclaration convert(final SootMethodUnit methodUnit) {
		final ProcedureDeclarationBuilder procedureBuilder = new ProcedureDeclarationBuilder();
		final ProcedureSignature signature = makeSignature(methodUnit);
		final List<Specification> specifications = makeSpecifications(methodUnit);
		final Body body = makeBody(methodUnit);
		procedureBuilder.name(NameConverter.methodName(methodUnit));
		procedureBuilder.signature(signature);
		procedureBuilder.body(body);
		procedureBuilder.specification(specifications);

		return procedureBuilder.build();
	}

}
