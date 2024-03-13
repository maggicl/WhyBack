package byteback.mlcfg.vimpParser;

import byteback.mlcfg.identifiers.FQDNEscaper;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import byteback.mlcfg.syntax.WhyClassDeclaration;
import byteback.mlcfg.syntax.WhyFieldDeclaration;
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

	private List<WhyFieldDeclaration> toFieldDeclarationList(Stream<SootField> fields) {
		return fields.map(f -> new WhyFieldDeclaration(
				identifierEscaper.escapeU(f.getName()),
				typeResolver.resolveType(f.getType()), f.isStatic()))
				.toList();
	}

	public WhyClassDeclaration parseClassDeclaration(SootClass clazz) {
		final String className = clazz.getName();
		final List<WhyFieldDeclaration> fields = toFieldDeclarationList(clazz.getFields().stream());

		return new WhyClassDeclaration(fqdnEscaper.escape(className), fields);
	}
}
