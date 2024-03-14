package byteback.mlcfg.vimp;

import byteback.mlcfg.identifiers.FQDNEscaper;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import byteback.mlcfg.syntax.WhyClass;
import byteback.mlcfg.syntax.WhyField;
import java.util.List;
import java.util.stream.Stream;
import soot.SootClass;
import soot.SootField;

public class VimpClassParser {

	private final FQDNEscaper fqdnEscaper;
	private final IdentifierEscaper identifierEscaper;
	private final TypeResolver typeResolver;

	public VimpClassParser(FQDNEscaper fqdnEscaper, IdentifierEscaper identifierEscaper, TypeResolver typeResolver) {
		this.fqdnEscaper = fqdnEscaper;
		this.identifierEscaper = identifierEscaper;
		this.typeResolver = typeResolver;
	}

	private List<WhyField> toFieldDeclarationList(Stream<SootField> fields) {
		return fields.map(f -> new WhyField(
				identifierEscaper.escapeU(f.getName()),
				typeResolver.resolveType(f.getType()), f.isStatic()))
				.toList();
	}

	public WhyClass parseClassDeclaration(SootClass clazz) {
		final String className = clazz.getName();
		final List<WhyField> fields = toFieldDeclarationList(clazz.getFields().stream());

		return new WhyClass(fqdnEscaper.escape(className), fields);
	}
}
