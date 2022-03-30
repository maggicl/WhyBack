package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.body.SootBody;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.core.representation.soot.unit.SootMethodUnit;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.FunctionSignature;
import byteback.frontend.boogie.ast.OptionalBinding;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.builder.FunctionDeclarationBuilder;
import byteback.frontend.boogie.builder.FunctionSignatureBuilder;
import byteback.frontend.boogie.builder.OptionalBindingBuilder;
import soot.Local;
import soot.Type;
import soot.VoidType;

public class FunctionConverter {

	private static final FunctionConverter instance = new FunctionConverter();

	public static FunctionConverter instance() {
		return instance;
	}

	public static OptionalBinding makeBinding(final Local local) {
		final SootType type = new SootType(local.getType());
		final TypeAccess typeAccess = new TypeAccessExtractor().visit(type);
		final OptionalBindingBuilder bindingBuilder = new OptionalBindingBuilder();
		bindingBuilder.name(local.getName()).typeAccess(typeAccess);

		return bindingBuilder.build();
	}

	public static FunctionSignature makeSignature(final SootMethodUnit methodUnit) {
		final FunctionSignatureBuilder signatureBuilder = new FunctionSignatureBuilder();
		signatureBuilder.addInputBinding(Prelude.getHeapVariable().makeOptionalBinding());

		for (Local local : methodUnit.getBody().getParameterLocals()) {
			signatureBuilder.addInputBinding(makeBinding(local));
		}

		methodUnit.getReturnType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseVoidType(final VoidType type) {
				throw new IllegalArgumentException("A pure function cannot be void");
			}

			@Override
			public void caseDefault(final Type type) {
				final TypeAccess boogieTypeAccess = new TypeAccessExtractor().visit(methodUnit.getReturnType());
				final OptionalBinding boogieBinding = new OptionalBindingBuilder().typeAccess(boogieTypeAccess).build();
				signatureBuilder.outputBinding(boogieBinding);
			}

		});

		return signatureBuilder.build();
	}

	public FunctionDeclaration convert(final SootMethodUnit methodUnit) {
		final FunctionDeclarationBuilder functionBuilder = new FunctionDeclarationBuilder();
		final FunctionSignature boogieSignature = makeSignature(methodUnit);
		final SootBody body = methodUnit.getBody();
		final SootType returnType = methodUnit.getReturnType();
		final Expression boogieExpression = new FunctionBodyExtractor(returnType).visit(body);
		functionBuilder.name(NameConverter.methodName(methodUnit));
		functionBuilder.signature(boogieSignature);
		functionBuilder.expression(boogieExpression);

		return functionBuilder.build();
	}

}
