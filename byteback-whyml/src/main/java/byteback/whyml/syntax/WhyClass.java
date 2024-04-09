package byteback.whyml.syntax;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.field.WhyField;
import byteback.whyml.syntax.type.WhyReference;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public record WhyClass(
		Identifier.FQDN name,
		Optional<Identifier.FQDN> extendsClass,
		Set<Identifier.FQDN> implementsInterfaces,
		List<WhyField> fields) {

	public WhyReference type() {
		return new WhyReference(name);
	}

	public Stream<Identifier.FQDN> superNames() {
		return Stream.concat(extendsClass.stream(), implementsInterfaces.stream());
	}
}
