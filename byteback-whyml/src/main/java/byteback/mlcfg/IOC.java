package byteback.mlcfg;

import byteback.mlcfg.identifiers.CaseInverter;
import byteback.mlcfg.identifiers.FQDNEscaper;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import byteback.mlcfg.printer.WhyClassPrinter;
import byteback.mlcfg.printer.WhyFieldPrinter;
import byteback.mlcfg.printer.WhyFunctionPrinter;
import byteback.mlcfg.vimp.TypeResolver;
import byteback.mlcfg.vimp.VimpClassNameParser;
import byteback.mlcfg.vimp.VimpClassParser;
import byteback.mlcfg.vimp.VimpFieldParser;
import byteback.mlcfg.vimp.VimpMethodSignatureParser;

public final class IOC {
	public static final CaseInverter CASE_INVERTER = new CaseInverter();
	public static final IdentifierEscaper IDENTIFIER_ESCAPER = new IdentifierEscaper(CASE_INVERTER);
	public static final FQDNEscaper FQDN_ESCAPER = new FQDNEscaper(IDENTIFIER_ESCAPER, CASE_INVERTER);
	public static final VimpClassNameParser CLASS_NAME_PARSER = new VimpClassNameParser(FQDN_ESCAPER);
	public static final TypeResolver TYPE_RESOLVER = new TypeResolver(CLASS_NAME_PARSER);
	public static final VimpFieldParser VIMP_FIELD_PARSER = new VimpFieldParser(CLASS_NAME_PARSER, IDENTIFIER_ESCAPER, TYPE_RESOLVER);
	public static final VimpClassParser CLASS_PARSER = new VimpClassParser(CLASS_NAME_PARSER, VIMP_FIELD_PARSER);
	public static final VimpMethodSignatureParser METHOD_PARSER = new VimpMethodSignatureParser(IDENTIFIER_ESCAPER, CLASS_NAME_PARSER, TYPE_RESOLVER);
	public static final WhyFieldPrinter WHY_FIELD_PRINTER = new WhyFieldPrinter();
	public static final WhyClassPrinter WHY_CLASS_PRINTER = new WhyClassPrinter(WHY_FIELD_PRINTER);
	public static final WhyFunctionPrinter WHY_METHOD_PRINTER = new WhyFunctionPrinter();
	public static final ProgramConverter PROGRAM_CONVERTER = new ProgramConverter(CLASS_PARSER, METHOD_PARSER, WHY_CLASS_PRINTER, WHY_METHOD_PRINTER);

	private IOC() {
	}
}
