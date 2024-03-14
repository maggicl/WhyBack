package byteback.mlcfg.syntax;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.types.WhyReference;
import java.util.List;

public record WhyClass(Identifier.FQDN name, List<WhyField> fields) {
	public WhyReference type() {
		return new WhyReference(name);
	}
}
