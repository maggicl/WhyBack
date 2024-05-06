package byteback.whyml.vimp.expr;

import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.field.ArrayOperation;
import byteback.whyml.syntax.expr.field.Operation;
import byteback.whyml.vimp.TypeResolver;
import byteback.whyml.vimp.VimpFieldParser;
import byteback.whyml.vimp.VimpMethodNameParser;
import byteback.whyml.vimp.VimpMethodParser;

public class ProcedureExpressionExtractor extends PureExpressionExtractor {
	public ProcedureExpressionExtractor(VimpMethodParser methodSignatureParser,
										VimpMethodNameParser methodNameParser,
										TypeResolver typeResolver,
										VimpFieldParser fieldParser,
										IdentifierEscaper identifierEscaper) {
		super(methodSignatureParser, methodNameParser, typeResolver, fieldParser, identifierEscaper);
	}

	@Override
	protected Operation fieldAccess() {
		return Operation.get();
	}

	@Override
	protected ArrayOperation arrayElemAccess(Expression index) {
		return ArrayOperation.load(index);
	}
}