package byteback.mlcfg.vimpParser;

import byteback.mlcfg.identifiers.IdentifierEscaper;
import byteback.mlcfg.syntax.WhyClassDeclaration;
import byteback.mlcfg.syntax.WhyFieldDeclaration;
import java.util.List;
import java.util.stream.Stream;
import soot.SootClass;
import soot.SootField;

public class VimpClassParser {

	private final IdentifierEscaper identifierEscaper;
	private final TypeResolver typeResolver;

	public VimpClassParser(IdentifierEscaper identifierEscaper, TypeResolver typeResolver) {
		this.identifierEscaper = identifierEscaper;
		this.typeResolver = typeResolver;
	}

	private List<WhyFieldDeclaration> toFieldDeclarationList(Stream<SootField> fields) {
		return fields.map(f -> new WhyFieldDeclaration(
				identifierEscaper.escapeU(f.getName()),
				typeResolver.resolveType(f.getType())))
				.toList();
	}

	public Stream<WhyClassDeclaration> parseClassDeclaration(SootClass clazz) {
		final String className = clazz.getName();

		final List<WhyFieldDeclaration> instanceFields =
				toFieldDeclarationList(clazz.getFields().stream().filter(e -> !e.isStatic()));

		final List<WhyFieldDeclaration> staticFields =
				toFieldDeclarationList(clazz.getFields().stream().filter(SootField::isStatic));

		return Stream.of(
				new WhyClassDeclaration("instance_" + className, instanceFields),
				new WhyClassDeclaration("static_" + className, staticFields)
		).filter(e -> !e.fields().isEmpty());
	}
}
