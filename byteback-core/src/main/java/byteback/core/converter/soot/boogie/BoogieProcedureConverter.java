package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.core.representation.soot.unit.SootMethodUnit;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.ProcedureDeclaration;
import byteback.frontend.boogie.builder.ProcedureDeclarationBuilder;
import byteback.frontend.boogie.builder.ProcedureSignatureBuilder;
import java.util.Map;
import soot.Type;
import soot.Unit;
import soot.VoidType;

public class BoogieProcedureConverter {

	private static final BoogieProcedureConverter instance = new BoogieProcedureConverter();

	public static BoogieProcedureConverter instance() {
		return instance;
	}

	public ProcedureDeclaration convert(final SootMethodUnit methodUnit) {
		final ProcedureDeclarationBuilder procedureBuilder = new ProcedureDeclarationBuilder();
		final ProcedureSignatureBuilder signatureBuilder = new ProcedureSignatureBuilder();
		final Map<Unit, Label> labelIndex = new BoogieLabelExtractor().visit(methodUnit.getBody());
		procedureBuilder.name(BoogieNameConverter.methodName(methodUnit));

		methodUnit.getReturnType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseVoidType(final VoidType type) {
				// Do not add output parameters
			}

			@Override
			public void caseDefault(final Type type) {
				signatureBuilder.addOutputBinding(BoogiePrelude.getReturnBindingBuilder()
						.typeAccess(new BoogieTypeAccessExtractor().visit(methodUnit.getReturnType())).build());
			}

		});

		return new BoogieProcedureExtractor(methodUnit, labelIndex, procedureBuilder, signatureBuilder)
				.visit(methodUnit.getBody());
	}

}
