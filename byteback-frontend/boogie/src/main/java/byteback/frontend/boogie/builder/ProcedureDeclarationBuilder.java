package byteback.frontend.boogie.builder;

import byteback.frontend.boogie.ast.*;

public class ProcedureDeclarationBuilder extends DeclarationBuilder {

	private Declarator declarator;

	private ProcedureSignature signature;

	private Opt<Body> body;

	private List<Specification> specification;

	public ProcedureDeclarationBuilder() {
		body = new Opt<>();
		specification = new List<>();
	}

	public ProcedureDeclarationBuilder name(final String name) {
		this.declarator = new Declarator(name);

		return this;
	}

	public ProcedureDeclarationBuilder signature(final ProcedureSignature signature) {
		this.signature = signature;

		return this;
	}

	public ProcedureDeclarationBuilder addSpecification(final Specification preCondition) {
		specification.add(preCondition);

		return this;
	}

	public ProcedureDeclarationBuilder specification(final List<Specification> specification) {
		this.specification.addAll(specification);

		return this;
	}

	public ProcedureDeclarationBuilder body(final Body body) {
		this.body = new Opt<>(body);

		return this;
	}

	public ProcedureDeclaration build() {
		if (declarator == null) {
			throw new IllegalArgumentException("Procedure declaration must include a name");
		}

		if (signature == null) {
			throw new IllegalArgumentException("Procedure declaration must include a signature");
		}

		return new ProcedureDeclaration(attributes, declarator, signature, specification, body);
	}

}
