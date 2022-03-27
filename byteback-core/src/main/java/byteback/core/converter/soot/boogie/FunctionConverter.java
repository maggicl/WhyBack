package byteback.core.converter.soot.boogie;

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
import java.util.Optional;
import soot.Local;
import soot.Type;
import soot.VoidType;

public class FunctionConverter {

	private static final FunctionConverter instance = new FunctionConverter();

	public static FunctionConverter instance() {
		return instance;
	}

	public OptionalBinding makeBinding(final Local local) {
		final SootType type = new SootType(local.getType());
		final TypeAccess typeAccess = new TypeAccessExtractor().visit(type);
		final OptionalBindingBuilder bindingBuilder = new OptionalBindingBuilder();
		bindingBuilder.name(local.getName()).typeAccess(typeAccess);

		return bindingBuilder.build();
	}

	public FunctionSignature makeSignature(final SootMethodUnit methodUnit) {
		final FunctionSignatureBuilder signatureBuilder = new FunctionSignatureBuilder();
		final Optional<OptionalBinding> thisBinding = methodUnit.getBody().getThisLocal().map(this::makeBinding);
		signatureBuilder.addInputBinding(Prelude.getHeapVariable().makeOptionalBinding());
		thisBinding.ifPresent(signatureBuilder::addInputBinding);

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
				final TypeAccess typeAccess = new TypeAccessExtractor().visit(methodUnit.getReturnType());
				signatureBuilder.outputBinding(new OptionalBindingBuilder().typeAccess(typeAccess).build());
			}

		});

		return signatureBuilder.build();
	}

	public FunctionDeclaration convert(final SootMethodUnit methodUnit) {
		final FunctionDeclarationBuilder functionBuilder = new FunctionDeclarationBuilder();
		final FunctionSignature signature = makeSignature(methodUnit);
		final Expression expression = new FunctionBodyExtractor(methodUnit.getReturnType()).visit(methodUnit.getBody());
		functionBuilder.name(NameConverter.methodName(methodUnit));
		functionBuilder.signature(signature);
		functionBuilder.expression(expression);

		return functionBuilder.build();
	}

}