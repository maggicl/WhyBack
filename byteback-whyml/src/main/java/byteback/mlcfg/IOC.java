package byteback.mlcfg;

import byteback.mlcfg.identifiers.CaseInverter;
import byteback.mlcfg.identifiers.FQDNEscaper;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import byteback.mlcfg.printer.WhyClassPrinter;
import byteback.mlcfg.printer.WhyFieldPrinter;
import byteback.mlcfg.printer.WhyMethodPrinter;
import byteback.mlcfg.vimp.TypeResolver;
import byteback.mlcfg.vimp.VimpClassParser;
import byteback.mlcfg.vimp.VimpMethodParser;

public final class IOC {
	public static final CaseInverter CASE_INVERTER = new CaseInverter();
	public static final IdentifierEscaper IDENTIFIER_ESCAPER = new IdentifierEscaper(CASE_INVERTER);
	public static final FQDNEscaper FQDN_ESCAPER = new FQDNEscaper(IDENTIFIER_ESCAPER, CASE_INVERTER);
	public static final TypeResolver TYPE_RESOLVER = new TypeResolver(FQDN_ESCAPER);
	public static final VimpClassParser CLASS_PARSER = new VimpClassParser(FQDN_ESCAPER, IDENTIFIER_ESCAPER, TYPE_RESOLVER);
	public static final VimpMethodParser METHOD_PARSER = new VimpMethodParser(IDENTIFIER_ESCAPER, FQDN_ESCAPER, TYPE_RESOLVER);
	public static final WhyFieldPrinter WHY_FIELD_PRINTER = new WhyFieldPrinter();
	public static final WhyClassPrinter WHY_CLASS_PRINTER = new WhyClassPrinter(WHY_FIELD_PRINTER);
	public static final WhyMethodPrinter WHY_METHOD_PRINTER = new WhyMethodPrinter();
	public static final ProgramConverter PROGRAM_CONVERTER = new ProgramConverter(CLASS_PARSER, METHOD_PARSER, WHY_CLASS_PRINTER, WHY_METHOD_PRINTER);

	private IOC() {
	}
}
