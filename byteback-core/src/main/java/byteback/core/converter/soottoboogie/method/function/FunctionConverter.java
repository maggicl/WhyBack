package byteback.core.converter.soottoboogie.method.function;

import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.expression.ExpressionExtractor;
import byteback.core.converter.soottoboogie.method.MethodConverter;
import byteback.core.converter.soottoboogie.type.TypeAccessExtractor;
import byteback.core.representation.soot.body.SootBodies;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.OptionalBinding;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.builder.FunctionDeclarationBuilder;
import byteback.frontend.boogie.builder.FunctionSignatureBuilder;
import byteback.frontend.boogie.builder.OptionalBindingBuilder;
import soot.Local;
import soot.SootMethod;
import soot.Type;
import soot.VoidType;

public class FunctionConverter extends MethodConverter {

	private static final FunctionConverter instance = new FunctionConverter();

	public static FunctionConverter v() {
		return instance;
	}

	public static OptionalBinding makeBinding(final Local local) {
		final Type type = local.getType();
		final TypeAccess typeAccess = new TypeAccessExtractor().visit(type);
		final OptionalBindingBuilder bindingBuilder = new OptionalBindingBuilder();
		bindingBuilder.name(ExpressionExtractor.localName(local)).typeAccess(typeAccess);

		return bindingBuilder.build();
	}

	public static void buildSignature(final FunctionDeclarationBuilder functionBuilder, final SootMethod method) {
		final FunctionSignatureBuilder signatureBuilder = new FunctionSignatureBuilder();
		signatureBuilder.addInputBinding(Prelude.v().getHeapVariable().makeOptionalBinding());

		for (Local local : SootBodies.getParameterLocals(method.retrieveActiveBody())) {
			signatureBuilder.addInputBinding(makeBinding(local));
		}

		method.getReturnType().apply(new SootTypeVisitor<>() {

			@Override
			public void caseVoidType(final VoidType type) {
				throw new ConversionException("A pure function cannot be void");
			}

			@Override
			public void caseDefault(final Type type) {
				final TypeAccess boogieTypeAccess = new TypeAccessExtractor().visit(method.getReturnType());
				final OptionalBinding boogieBinding = new OptionalBindingBuilder().typeAccess(boogieTypeAccess).build();
				signatureBuilder.outputBinding(boogieBinding);
			}

		});

		functionBuilder.signature(signatureBuilder.build());
	}

	public static void buildExpression(final FunctionDeclarationBuilder functionBuilder, final SootMethod method) {
		functionBuilder
				.expression(new FunctionBodyExtractor(method.getReturnType()).visit(method.retrieveActiveBody()));
	}

	public FunctionDeclaration convert(final SootMethod method) {
		final var builder = new FunctionDeclarationBuilder();

		try {
			builder.name(methodName(method));
			buildSignature(builder, method);
			buildExpression(builder, method);
		} catch (final ConversionException exception) {
			throw new FunctionConversionException(method, exception);
		}

		return builder.build();
	}

}
