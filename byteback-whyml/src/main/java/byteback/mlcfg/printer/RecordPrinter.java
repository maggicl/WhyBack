package byteback.mlcfg.printer;

import byteback.mlcfg.syntax.WhyRecordDeclaration;

import byteback.mlcfg.syntax.identifiers.IdentifierEscaper;
import java.util.stream.Collectors;

public class RecordPrinter {
    private final IdentifierEscaper identifierEscaper;

	public RecordPrinter(IdentifierEscaper identifierEscaper) {
		this.identifierEscaper = identifierEscaper;
	}

    public String printRecord(final WhyRecordDeclaration r) {
        return r.fields().stream()
                .map(f -> String.format(
						"  %s: %s",
						identifierEscaper.escape(f.name()),
						f.type().getPrefix().map(s -> s + " ").orElse("") + f.type().getWhyType()))
                .collect(Collectors.joining(
						";\n",
						String.format(
								"type %s = {\n",
								identifierEscaper.escape(r.name())),
						"\n}"));
    }
}
