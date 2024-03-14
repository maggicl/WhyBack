package byteback.mlcfg.syntax.types;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import java.util.Optional;

public interface WhyPtrType extends WhyType {
	@Override
	default String getWhyType() {
		return "Ptr.t";
	}
}
