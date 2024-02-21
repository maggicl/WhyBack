package byteback.mlcfg;

import byteback.mlcfg.printer.RecordPrinter;
import byteback.mlcfg.syntax.identifiers.IdentifierEscaper;
import byteback.mlcfg.vimpParser.TypeResolver;
import byteback.mlcfg.vimpParser.VimpClassParser;

public final class IOC {
    public static final IdentifierEscaper identifierEscaper = new IdentifierEscaper();
    public static final TypeResolver typeResolver = new TypeResolver(identifierEscaper);
    public static final VimpClassParser classParser = new VimpClassParser(typeResolver);
    public static final RecordPrinter recordPrinter = new RecordPrinter(identifierEscaper);
    public static final ProgramConverter programConverter = new ProgramConverter(classParser, recordPrinter);
    private IOC() {
    }
}
