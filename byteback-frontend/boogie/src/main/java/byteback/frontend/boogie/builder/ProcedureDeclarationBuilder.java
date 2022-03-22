package byteback.frontend.boogie.builder;

import byteback.frontend.boogie.ast.*;
import java.util.Optional;

public class ProcedureDeclarationBuilder extends DeclarationBuilder {

	private Optional<Declarator> declaratorParameter;

	private Optional<ProcedureSignature> signatureParameter;

	private Optional<Body> bodyParameter;

	private List<Specification> specification;

	public ProcedureDeclarationBuilder() {
		declaratorParameter = Optional.empty();
		signatureParameter = Optional.empty();
		bodyParameter = Optional.empty();
		specification = new List<>();
	}

	public ProcedureDeclarationBuilder name(final String name) {
		this.declaratorParameter = Optional.of(new Declarator(name));

		return this;
	}

	public ProcedureDeclarationBuilder signature(final ProcedureSignature signature) {
		this.signatureParameter = Optional.of(signature);

		return this;
	}

	public ProcedureDeclarationBuilder addSpecification(final Specification preCondition) {
		specification.add(preCondition);

		return this;
	}

	public ProcedureDeclarationBuilder body(final Body body) {
		this.bodyParameter = Optional.of(body);

		return this;
	}

	public ProcedureDeclaration build() {
		final Declarator declarator = declaratorParameter
				.orElseThrow(() -> new IllegalArgumentException("Procedure declaration must include a name"));
		final ProcedureSignature signature = signatureParameter
				.orElseThrow(() -> new IllegalArgumentException("Procedure declaration must include a signature"));
		final Body body = bodyParameter.orElse(null);

		return new ProcedureDeclaration(attributes, declarator, signature, specification, new Opt<>(body));
	}

}
