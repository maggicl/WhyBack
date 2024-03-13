package byteback.mlcfg;

import byteback.mlcfg.printer.WhyClassPrinter;
import byteback.mlcfg.identifiers.CaseInverter;
import byteback.mlcfg.identifiers.FQDNEscaper;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import byteback.mlcfg.vimpParser.TypeResolver;
import byteback.mlcfg.vimpParser.VimpClassParser;

public final class IOC {
    public static final CaseInverter caseInverter = new CaseInverter();
    public static final IdentifierEscaper identifierEscaper = new IdentifierEscaper(caseInverter);
    public static final FQDNEscaper fqdnEscaper = new FQDNEscaper(identifierEscaper, caseInverter);
    public static final TypeResolver typeResolver = new TypeResolver(fqdnEscaper);
    public static final VimpClassParser classParser = new VimpClassParser(identifierEscaper, typeResolver);
    public static final WhyClassPrinter WHY_CLASS_PRINTER = new WhyClassPrinter(identifierEscaper);
    public static final ProgramConverter programConverter = new ProgramConverter(classParser, WHY_CLASS_PRINTER);
    private IOC() {
    }
}
