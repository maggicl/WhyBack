package byteback.whyml.vimp;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.function.WhyFunctionSignature;

public class VimpMethodNameParser {
	private final IdentifierEscaper identifierEscaper;

	public VimpMethodNameParser(IdentifierEscaper identifierEscaper) {
		this.identifierEscaper = identifierEscaper;
	}

	public Identifier.L methodName(WhyFunctionSignature method) {
		return identifierEscaper.escapeMethod(method.className(), method.name(), method.descriptor());
	}
}
