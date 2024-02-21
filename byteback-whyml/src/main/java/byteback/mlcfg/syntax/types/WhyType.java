package byteback.mlcfg.syntax.types;

import java.util.Optional;

public interface WhyType {
	Optional<String> getPrefix();
	String getIdentifier();
}
