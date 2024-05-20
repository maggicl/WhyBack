package byteback.whyml.vimp;

import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.vimp.expr.ProgramExpressionExtractor;
import soot.Local;

public class VimpLocalParser {
	private final IdentifierEscaper identifierEscaper;
	private final TypeResolver typeResolver;

	public VimpLocalParser(IdentifierEscaper identifierEscaper, TypeResolver typeResolver) {
		this.identifierEscaper = identifierEscaper;
		this.typeResolver = typeResolver;
	}

	public WhyLocal parse(Local local) {
		return new WhyLocal(
				identifierEscaper.escapeLocalVariable(local.getName()),
				typeResolver.resolveType(local.getType())
		);
	}
}
