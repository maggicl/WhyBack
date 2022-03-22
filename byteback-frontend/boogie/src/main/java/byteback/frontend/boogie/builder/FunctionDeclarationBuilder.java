package byteback.frontend.boogie.builder;

import byteback.frontend.boogie.ast.*;
import java.util.Optional;

public class FunctionDeclarationBuilder extends DeclarationBuilder {

	private Optional<Declarator> declaratorParameter;

	private Optional<FunctionSignature> signatureParameter;

	private Optional<Expression> expressionParameter;

	public FunctionDeclarationBuilder() {
		this.declaratorParameter = Optional.empty();
		this.signatureParameter = Optional.empty();
		this.expressionParameter = Optional.empty();
	}

	public FunctionDeclarationBuilder name(final String name) {
		this.declaratorParameter = Optional.of(new Declarator(name));

		return this;
	}

	public FunctionDeclarationBuilder signature(final FunctionSignature signature) {
		this.signatureParameter = Optional.of(signature);

		return this;
	}

	public FunctionDeclarationBuilder expression(final Expression expression) {
		this.expressionParameter = Optional.of(expression);

		return this;
	}

	@Override
	public FunctionDeclarationBuilder addAttribute(final Attribute attribute) {
		super.addAttribute(attribute);

		return this;
	}

	public FunctionDeclaration build() {
		final Declarator declarator = declaratorParameter
				.orElseThrow(() -> new IllegalArgumentException("Function declaration must include a name"));
		final FunctionSignature signature = signatureParameter
				.orElseThrow(() -> new IllegalArgumentException("Function declaration must include a signature"));
		final Expression expression = expressionParameter.orElse(null);

		return new FunctionDeclaration(attributes, declarator, signature, new Opt<>(expression));
	}

}
