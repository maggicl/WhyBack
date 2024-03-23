package byteback.mlcfg.vimp;

import byteback.mlcfg.identifiers.FQDNEscaper;
import byteback.mlcfg.identifiers.Identifier;
import soot.SootClass;

public class VimpClassNameParser {
	private final FQDNEscaper fqdnEscaper;

	public VimpClassNameParser(FQDNEscaper fqdnEscaper) {
		this.fqdnEscaper = fqdnEscaper;
	}

	public Identifier.FQDN parse(SootClass clazz) {
		return fqdnEscaper.escape(clazz.getName(), clazz.getPackageName().isEmpty());
	}
}
