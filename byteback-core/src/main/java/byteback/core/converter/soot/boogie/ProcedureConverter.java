package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.core.representation.soot.unit.SootMethodUnit;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.BoundedBinding;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.ProcedureDeclaration;
import byteback.frontend.boogie.ast.ProcedureSignature;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.builder.BoundedBindingBuilder;
import byteback.frontend.boogie.builder.ProcedureDeclarationBuilder;
import byteback.frontend.boogie.builder.ProcedureSignatureBuilder;
import byteback.frontend.boogie.builder.VariableDeclarationBuilder;
import java.util.Map;
import soot.Local;
import soot.Type;
import soot.Unit;
import soot.VoidType;

public class ProcedureConverter {

	private static final ProcedureConverter instance = new ProcedureConverter();

	public static ProcedureConverter instance() {
		return instance;
	}

	public BoundedBinding makeBinding(final Local local) {
		final SootType type = new SootType(local.getType());
		final TypeAccess typeAccess = new TypeAccessExtractor().visit(type);
		final BoundedBindingBuilder bindingBuilder = new BoundedBindingBuilder();
		bindingBuilder.addName(local.getName()).typeAccess(typeAccess);

		return bindingBuilder.build();
	}

	public ProcedureSignature makeSignature(final SootMethodUnit methodUnit) {
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

	public Body makeBody(final SootMethodUnit methodUnit) {
		final Body body = new Body();
		final Map<Unit, Label> labelTable = new LabelCollector().visit(methodUnit.getBody());
		methodUnit.getBody().apply(new ProcedureBodyExtractor(body, methodUnit.getReturnType(), labelTable));

		for (Local local : methodUnit.getBody().getLocals()) {
			final VariableDeclarationBuilder variableBuilder = new VariableDeclarationBuilder();
			body.addLocalDeclaration(variableBuilder.addBinding(makeBinding(local)).build());
		}

		return body;
	}

	public ProcedureDeclaration convert(final SootMethodUnit methodUnit) {
		final ProcedureDeclarationBuilder procedureBuilder = new ProcedureDeclarationBuilder();
		final ProcedureSignature signature = makeSignature(methodUnit);
		final Body body = makeBody(methodUnit);
		procedureBuilder.name(NameConverter.methodName(methodUnit));
		procedureBuilder.signature(signature);
		procedureBuilder.body(body);

		return procedureBuilder.build();
	}

}
