package byteback.mlcfg.vimpParser;

import byteback.mlcfg.syntax.identifiers.IdentifierEscaper;
import byteback.mlcfg.syntax.types.WhyType;
import soot.Type;

public class TypeResolver {

	private final IdentifierEscaper escaper;

	public TypeResolver(IdentifierEscaper escaper) {
		this.escaper = escaper;
	}

	public WhyType resolveType(Type sootType) {
		final TypeAccessExtractor e = new TypeAccessExtractor(escaper);
		e.visit(sootType);
		return e.result();
	}
}
