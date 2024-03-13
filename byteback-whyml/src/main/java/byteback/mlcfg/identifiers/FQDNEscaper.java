package byteback.mlcfg.identifiers;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;

public class FQDNEscaper {
	private final IdentifierEscaper identifierEscaper;
	private final CaseInverter caseInverter;

	public FQDNEscaper(IdentifierEscaper identifierEscaper, CaseInverter caseInverter) {
		this.identifierEscaper = identifierEscaper;
		this.caseInverter = caseInverter;
	}

	public Identifier.FQDN escape(String fqdn) {
		final String[] modules = fqdn.split("\\.");

		if (modules.length == 0) {
			throw new IllegalArgumentException("need a non-empty fqdn string");
		}

		final List<Identifier.U> parents = Arrays.stream(modules).limit(modules.length - 1)
				.map(e -> identifierEscaper.escapeU(caseInverter.invertCase(e)))
				.collect(Collectors.toCollection(ArrayList::new));
		parents.add(identifierEscaper.escapeU(modules[modules.length - 1]));

		return new Identifier.FQDN(parents);
	}
}
