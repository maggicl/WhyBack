package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.unit.SootMethodUnit;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.builder.FunctionDeclarationBuilder;
import byteback.frontend.boogie.builder.FunctionSignatureBuilder;

public class BoogieFunctionConverter {

	private static final BoogieFunctionConverter instance = new BoogieFunctionConverter();

	public static BoogieFunctionConverter instance() {
		return instance;
	}

	public FunctionDeclaration convert(final SootMethodUnit methodUnit) {
		final FunctionSignatureBuilder signatureBuilder = new FunctionSignatureBuilder();
		final FunctionDeclarationBuilder functionBuilder = new FunctionDeclarationBuilder();
		signatureBuilder.addInputBinding(BoogiePrelude.getHeapVariable().makeOptionalBinding());
		functionBuilder.name(BoogieNameConverter.methodName(methodUnit));

    return new BoogieFunctionExtractor(methodUnit, functionBuilder, signatureBuilder).visit(methodUnit.getBody());
  }

}
