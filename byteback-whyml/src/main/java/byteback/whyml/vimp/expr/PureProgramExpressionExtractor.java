package byteback.whyml.vimp.expr;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.vimp.TypeResolver;
import byteback.whyml.vimp.VimpFieldParser;
import byteback.whyml.vimp.VimpMethodNameParser;
import byteback.whyml.vimp.VimpMethodParser;
import soot.Local;

/**
 * Like a ProgramExpressionExtractor but identifiers are local variables
 */
public class PureProgramExpressionExtractor extends PureExpressionExtractor {
	public PureProgramExpressionExtractor(VimpFieldParser fieldParser,
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
