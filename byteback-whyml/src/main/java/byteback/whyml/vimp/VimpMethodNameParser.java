package byteback.whyml.vimp;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.function.WhyFunctionDeclaration;
import byteback.whyml.syntax.function.WhyFunctionParam;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.type.WhyType;
import java.util.List;

public class VimpMethodNameParser {
	private final IdentifierEscaper identifierEscaper;

	public VimpMethodNameParser(IdentifierEscaper identifierEscaper) {
		this.identifierEscaper = identifierEscaper;
	}

	public Identifier.L methodName(WhyFunctionSignature method) {
		return methodName(method.className(), method.name(), method.params(), method.returnType(), method.declaration());
	}

	public Identifier.L methodName(Identifier.FQDN className, String methodName, List<WhyFunctionParam> params,
								   WhyType returnType, WhyFunctionDeclaration declaration) {
		final String descriptor = WhyFunctionSignature.descriptor(params, returnType);
		final Identifier.L name = identifierEscaper.escapeL(methodName + descriptor);
		return declaration.isSpec() ? identifierEscaper.specFunction(className, name) : name;
	}
}
