package byteback.mlcfg.vimp;

import byteback.mlcfg.identifiers.FQDNEscaper;
import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import byteback.mlcfg.syntax.WhyClass;
import byteback.mlcfg.syntax.WhyField;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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

	public WhyClass parse(SootClass clazz) {
		final String className = clazz.getName();
		final Optional<Identifier.FQDN> superclass = clazz.hasSuperclass()
				? Optional.of(fqdnEscaper.escape(clazz.getSuperclass().getName()))
				: Optional.empty();

		final Set<Identifier.FQDN> interfaces = clazz.getInterfaces().stream()
				.map(SootClass::getName)
				.map(fqdnEscaper::escape)
				.collect(Collectors.toSet());

		final List<WhyField> fields = toFieldDeclarationList(clazz.getFields().stream());

		return new WhyClass(
				fqdnEscaper.escape(className),
				superclass,
				interfaces,
				fields);
	}
}
