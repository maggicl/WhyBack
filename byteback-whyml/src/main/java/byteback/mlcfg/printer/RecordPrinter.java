package byteback.mlcfg.printer;

import byteback.mlcfg.syntax.RecordDeclaration;

import java.util.stream.Collectors;

public class RecordPrinter {
    public String printRecord(final RecordDeclaration r) {
        return r.fields().stream()
                .map(f -> String.format("  %s: %s", f.name(), f.type()))
                .collect(Collectors.joining(";\n", String.format("type %s = {\n", r.name()), "\n}"));
    }
}
