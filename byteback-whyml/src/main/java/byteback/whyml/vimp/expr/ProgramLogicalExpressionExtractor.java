package byteback.whyml.vimp.expr;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.vimp.TypeResolver;
import byteback.whyml.vimp.VimpFieldParser;
import byteback.whyml.vimp.VimpMethodNameParser;
import byteback.whyml.vimp.VimpMethodParser;
import soot.Local;

/**
 * Like a ProgramExpressionExtractor but identifiers are local variables. Used to parse expression in
 * "logical statements" (i.e. assertions, assumes, and invariants) in program code
 */
public class ProgramLogicalExpressionExtractor extends PureExpressionExtractor {
	public ProgramLogicalExpressionExtractor(VimpFieldParser fieldParser,
											 TypeResolver typeResolver,
											 VimpMethodParser methodSignatureParser,
											 VimpMethodNameParser methodNameParser,
											 IdentifierEscaper identifierEscaper) {
		super(fieldParser, typeResolver, methodSignatureParser, methodNameParser, identifierEscaper);
	}

	@Override
	protected Identifier.L localIdentifier(Local variable) {
		return identifierEscaper.escapeLocalVariable(variable.getName());
	}
}
