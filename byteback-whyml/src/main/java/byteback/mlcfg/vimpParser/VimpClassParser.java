package byteback.mlcfg.vimpParser;

import byteback.mlcfg.syntax.WhyRecordDeclaration;
import byteback.mlcfg.syntax.WhyRecordFieldDeclaration;
import java.util.List;
import java.util.stream.Stream;
import soot.SootClass;
import soot.SootField;

public class VimpClassParser {

	private final TypeResolver typeResolver;

	public VimpClassParser(TypeResolver typeResolver) {
		this.typeResolver = typeResolver;
	}

	private List<WhyRecordFieldDeclaration> toFieldDeclarationList(Stream<SootField> fields) {
		return fields.map(f -> new WhyRecordFieldDeclaration(f.getName(), typeResolver.resolveType(f.getType())))
				.toList();
	}

	public Stream<WhyRecordDeclaration> parseClassDeclaration(SootClass clazz) {
		final String className = clazz.getName();

		final List<WhyRecordFieldDeclaration> instanceFields =
				toFieldDeclarationList(clazz.getFields().stream().filter(e -> !e.isStatic()));

		final List<WhyRecordFieldDeclaration> staticFields =
				toFieldDeclarationList(clazz.getFields().stream().filter(SootField::isStatic));

		return Stream.of(
				new WhyRecordDeclaration("instance_" + className, instanceFields),
				new WhyRecordDeclaration("static_" + className, staticFields)
		).filter(e -> !e.fields().isEmpty());
	}
}
