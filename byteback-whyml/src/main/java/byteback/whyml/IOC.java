package byteback.whyml;

import byteback.whyml.identifiers.CaseInverter;
import byteback.whyml.identifiers.FQDNEscaper;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.printer.WhyClassPrinter;
import byteback.whyml.printer.WhyFieldPrinter;
import byteback.whyml.printer.WhyFunctionPrinter;
import byteback.whyml.printer.WhySignaturePrinter;
import byteback.whyml.vimp.TypeResolver;
import byteback.whyml.vimp.VimpBlockParser;
import byteback.whyml.vimp.VimpClassNameParser;
import byteback.whyml.vimp.VimpClassParser;
import byteback.whyml.vimp.VimpFieldParser;
import byteback.whyml.vimp.VimpMethodBodyParser;
import byteback.whyml.vimp.VimpMethodNameParser;
import byteback.whyml.vimp.VimpMethodParamParser;
import byteback.whyml.vimp.VimpMethodParser;
import byteback.whyml.vimp.WhyResolver;
import byteback.whyml.vimp.expr.ProcedureExpressionExtractor;
import byteback.whyml.vimp.expr.PureBodyExtractor;
import byteback.whyml.vimp.expr.PureExpressionExtractor;

public final class IOC {
	public static final CaseInverter CASE_INVERTER = new CaseInverter();
	public static final IdentifierEscaper IDENTIFIER_ESCAPER = new IdentifierEscaper(CASE_INVERTER);
	public static final FQDNEscaper FQDN_ESCAPER = new FQDNEscaper(IDENTIFIER_ESCAPER, CASE_INVERTER);
	public static final VimpClassNameParser CLASS_NAME_PARSER = new VimpClassNameParser(FQDN_ESCAPER);
	public static final TypeResolver TYPE_RESOLVER = new TypeResolver(CLASS_NAME_PARSER);
	public static final VimpFieldParser VIMP_FIELD_PARSER = new VimpFieldParser(CLASS_NAME_PARSER, IDENTIFIER_ESCAPER, TYPE_RESOLVER);
	public static final VimpClassParser CLASS_PARSER = new VimpClassParser(CLASS_NAME_PARSER, VIMP_FIELD_PARSER);
	public static final VimpMethodParamParser METHOD_PARAM_PARSER = new VimpMethodParamParser(IDENTIFIER_ESCAPER, TYPE_RESOLVER);
	public static final VimpMethodParser METHOD_PARSER = new VimpMethodParser(METHOD_PARAM_PARSER, CLASS_NAME_PARSER, TYPE_RESOLVER);
	public static final VimpMethodNameParser METHOD_NAME_PARSER = new VimpMethodNameParser(IDENTIFIER_ESCAPER);
	public static final PureExpressionExtractor PURE_EXPRESSION_EXTRACTOR = new PureExpressionExtractor(METHOD_PARSER, METHOD_NAME_PARSER, TYPE_RESOLVER, VIMP_FIELD_PARSER, IDENTIFIER_ESCAPER);
	public static final PureBodyExtractor FUNCTION_BODY_EXTRACTOR = new PureBodyExtractor(PURE_EXPRESSION_EXTRACTOR);
	public static final ProcedureExpressionExtractor PROCEDURE_EXPRESSION_EXTRACTOR = new ProcedureExpressionExtractor(METHOD_PARSER, METHOD_NAME_PARSER, TYPE_RESOLVER, VIMP_FIELD_PARSER, IDENTIFIER_ESCAPER);
	public static final VimpBlockParser VIMP_BLOCK_PARSER = new VimpBlockParser(PROCEDURE_EXPRESSION_EXTRACTOR);
	public static final VimpMethodBodyParser METHOD_BODY_PARSER = new VimpMethodBodyParser(IDENTIFIER_ESCAPER, TYPE_RESOLVER, FUNCTION_BODY_EXTRACTOR, VIMP_BLOCK_PARSER);
	public static final WhyResolver WHY_RESOLVER = new WhyResolver(CLASS_PARSER, METHOD_PARSER, METHOD_BODY_PARSER);
	public static final WhySignaturePrinter WHY_SIGNATURE_PRINTER = new WhySignaturePrinter(METHOD_NAME_PARSER);
	public static final WhyFunctionPrinter WHY_FUNCTION_PRINTER = new WhyFunctionPrinter(WHY_SIGNATURE_PRINTER);
	public static final WhyFieldPrinter WHY_FIELD_PRINTER = new WhyFieldPrinter();
	public static final WhyClassPrinter WHY_CLASS_PRINTER = new WhyClassPrinter(WHY_FIELD_PRINTER);
	public static final ProgramConverter PROGRAM_CONVERTER = new ProgramConverter(WHY_RESOLVER, WHY_CLASS_PRINTER, WHY_SIGNATURE_PRINTER, WHY_FUNCTION_PRINTER);

	private IOC() {
	}
}
