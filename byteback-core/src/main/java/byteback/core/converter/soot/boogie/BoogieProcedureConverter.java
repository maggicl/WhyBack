package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.unit.SootMethodUnit;
import byteback.frontend.boogie.ast.ProcedureDeclaration;
import byteback.frontend.boogie.builder.ProcedureDeclarationBuilder;
import byteback.frontend.boogie.builder.ProcedureSignatureBuilder;

public class BoogieProcedureConverter {

	private static final BoogieProcedureConverter instance = new BoogieProcedureConverter();

	public static BoogieProcedureConverter instance() {
		return instance;
	}

	public ProcedureDeclaration convert(final SootMethodUnit methodUnit) {
		final ProcedureDeclarationBuilder procedureBuilder = new ProcedureDeclarationBuilder();
		final ProcedureSignatureBuilder signatureBuilder = new ProcedureSignatureBuilder();
		procedureBuilder.name(BoogieNameConverter.methodName(methodUnit));

		return new BoogieProcedureExtractor(procedureBuilder, signatureBuilder).visit(methodUnit.getBody());
	}

}
