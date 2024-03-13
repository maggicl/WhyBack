package byteback.mlcfg.syntax.identifiers;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FQDNEscaper {
	private final IdentifierEscaper identifierEscaper;
	private final CaseInverter caseInverter;

	public FQDNEscaper(IdentifierEscaper identifierEscaper, CaseInverter caseInverter) {
		this.identifierEscaper = identifierEscaper;
		this.caseInverter = caseInverter;
	}

	public String escape(String fqdn) {
		final String[] modules = fqdn.split("\\.");

		if (modules.length == 0) {
			throw new IllegalArgumentException("need a non-empty fqdn string");
		}

		final String module = Arrays.stream(modules).limit(modules.length - 1)
				.map(e -> identifierEscaper.escape(
						caseInverter.invertCase(e),
						IdentifierEscaper.IdentifierClass.UIDENT))
				.collect(Collectors.joining(".", "", "."));

		final String className = identifierEscaper.escape(
				modules[modules.length - 1],
				IdentifierEscaper.IdentifierClass.UIDENT);

		return module + className;
	}
}
