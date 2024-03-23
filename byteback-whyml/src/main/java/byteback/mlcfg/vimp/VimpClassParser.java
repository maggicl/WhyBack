package byteback.mlcfg.vimp;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.WhyClass;
import byteback.mlcfg.syntax.WhyField;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import soot.SootClass;

public class VimpClassParser {

	private final VimpClassNameParser classNameParser;
	private final VimpFieldParser fieldParser;

	public VimpClassParser(VimpClassNameParser classNameParser, VimpFieldParser fieldParser) {
		this.classNameParser = classNameParser;
		this.fieldParser = fieldParser;
	}

	public WhyClass parse(SootClass clazz) {
		final Optional<Identifier.FQDN> superclass = clazz.hasSuperclass()
				? Optional.of(classNameParser.parse(clazz.getSuperclass()))
				: Optional.empty();

		final Set<Identifier.FQDN> interfaces = clazz.getInterfaces().stream()
				.map(classNameParser::parse)
				.collect(Collectors.toSet());

		final Identifier.FQDN clazzFqdn = classNameParser.parse(clazz);

		final List<WhyField> fields = clazz.getFields().stream()
				.map(fieldParser::parse)
				.toList();

		return new WhyClass(clazzFqdn, superclass, interfaces, fields);
	}
}
