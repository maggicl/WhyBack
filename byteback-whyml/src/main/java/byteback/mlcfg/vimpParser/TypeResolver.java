package byteback.mlcfg.vimpParser;

import byteback.mlcfg.identifiers.FQDNEscaper;
import byteback.mlcfg.syntax.types.WhyType;
import soot.Type;

public class TypeResolver {

	private final FQDNEscaper escaper;

	public TypeResolver(FQDNEscaper escaper) {
		this.escaper = escaper;
	}

	public WhyType resolveType(Type sootType) {
		final TypeAccessExtractor e = new TypeAccessExtractor(escaper);
		e.visit(sootType);
		return e.result();
	}
}
