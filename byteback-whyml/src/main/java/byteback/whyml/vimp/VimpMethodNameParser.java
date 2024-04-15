package byteback.whyml.vimp;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.function.VimpMethod;

public class VimpMethodNameParser {
	private final IdentifierEscaper identifierEscaper;

	public VimpMethodNameParser(IdentifierEscaper identifierEscaper) {
		this.identifierEscaper = identifierEscaper;
	}

	public Identifier.L methodName(VimpMethod method) {
		final Identifier.L name = identifierEscaper.escapeL(method.name() + method.descriptor());
		return method.decl().isSpec() ? identifierEscaper.specFunction(method.className(), name) : name;
	}
}
