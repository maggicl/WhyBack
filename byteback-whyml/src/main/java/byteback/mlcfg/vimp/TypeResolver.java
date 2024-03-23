package byteback.mlcfg.vimp;

import byteback.mlcfg.identifiers.FQDNEscaper;
import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;
import soot.Type;

public class TypeResolver {

	private final VimpClassNameParser escaper;

	public TypeResolver(VimpClassNameParser escaper) {
		this.escaper = escaper;
	}

	public WhyType resolveType(Type sootType) {
		final TypeAccessExtractor e = new TypeAccessExtractor(escaper, true);
		e.visit(sootType);
		return e.result();
	}

	public WhyJVMType resolveJVMType(Type sootType) {
		final TypeAccessExtractor e = new TypeAccessExtractor(escaper, false);
		e.visit(sootType);
		return (WhyJVMType) e.result(); // will never fail if resolveRefType is set to false
	}
}
