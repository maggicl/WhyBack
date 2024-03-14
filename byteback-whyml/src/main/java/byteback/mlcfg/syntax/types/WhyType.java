package byteback.mlcfg.syntax.types;

import byteback.mlcfg.identifiers.Identifier;
import java.util.Optional;

public interface WhyType {
	String getWhyType();

	String getPreludeType(Identifier.FQDN currentScope);

	void accept(WhyTypeVisitor visitor);

	/**
	 * Returns the scope of where the get/put (load/store/(a)newarray for arrays) WhyML prelude function definitions
	 * for this type are located.
	 *
	 * @return A WhyML scope
	 */
	String getWhyAccessorScope();

	default Optional<String> getPrecondition() {
		return Optional.empty();
	}
}
