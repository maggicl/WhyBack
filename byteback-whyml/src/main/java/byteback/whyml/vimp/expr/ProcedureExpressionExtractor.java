package byteback.whyml.vimp.expr;

import byteback.analysis.vimp.VoidConstant;
import byteback.whyml.identifiers.Identifier;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.LocalVariableExpression;
import byteback.whyml.syntax.expr.NullLiteral;
import byteback.whyml.syntax.expr.UnitLiteral;
import byteback.whyml.syntax.expr.field.ArrayOperation;
import byteback.whyml.syntax.expr.field.Operation;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.vimp.TypeResolver;
import byteback.whyml.vimp.VimpFieldParser;
import byteback.whyml.vimp.VimpMethodNameParser;
import byteback.whyml.vimp.VimpMethodParser;
import soot.jimple.CaughtExceptionRef;

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

	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		setExpression(new LocalVariableExpression(Identifier.Special.CAUGHT_EXCEPTION, WhyJVMType.PTR));
	}

	@Override
	public void caseVoidConstant(final VoidConstant v) {
		// TODO: change and define a special "unit literal" for no exception being thrown
		setExpression(NullLiteral.INSTANCE);
	}
}