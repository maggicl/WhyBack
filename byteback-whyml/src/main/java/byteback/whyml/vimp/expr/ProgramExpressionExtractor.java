package byteback.whyml.vimp.expr;

import byteback.analysis.vimp.VoidConstant;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.LocalVariableExpression;
import byteback.whyml.syntax.expr.NewArrayExpression;
import byteback.whyml.syntax.expr.NewExpression;
import byteback.whyml.syntax.expr.NullLiteral;
import byteback.whyml.syntax.expr.field.ArrayOperation;
import byteback.whyml.syntax.expr.field.Operation;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyReference;
import byteback.whyml.syntax.type.WhyType;
import byteback.whyml.vimp.TypeResolver;
import byteback.whyml.vimp.VimpFieldParser;
import byteback.whyml.vimp.VimpMethodNameParser;
import byteback.whyml.vimp.VimpMethodParser;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;

public class ProgramExpressionExtractor extends PureExpressionExtractor {
	public ProgramExpressionExtractor(VimpMethodParser methodSignatureParser,
									  VimpMethodNameParser methodNameParser,
									  TypeResolver typeResolver,
									  VimpFieldParser fieldParser,
									  IdentifierEscaper identifierEscaper) {
		super(methodSignatureParser, methodNameParser, typeResolver, fieldParser, identifierEscaper);
	}

	@Override
	protected Operation fieldAccess() {
		return Operation.GET;
	}

	@Override
	protected ArrayOperation arrayElemAccess(Expression index) {
		return ArrayOperation.load(index);
	}

	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		setExpression(new LocalVariableExpression(WhyLocal.CAUGHT_EXCEPTION));
	}

	@Override
	public void caseNewArrayExpr(NewArrayExpr v) {
		final WhyType t = typeResolver.resolveJVMType(v.getBaseType());
		final Expression size = visit(v.getSize());

		if (size.type() != WhyJVMType.INT) {
			throw new IllegalStateException("array size must be INT, given " + size.type());
		}

		setExpression(new NewArrayExpression(t, size));
	}

	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		// TODO: implement MULTIANEWARRAY
		throw new UnsupportedOperationException("multi-array initialization not supported");
	}

	@Override
	public void caseNewExpr(NewExpr v) {
		final WhyType t = typeResolver.resolveType(v.getType());
		if (t instanceof WhyReference ref) {
			setExpression(new NewExpression(ref));
		} else {
			throw new IllegalStateException("new called with non-reference type: " + t);
		}
	}

	@Override
	public void caseVoidConstant(final VoidConstant v) {
		// TODO: change and define a special "unit literal" for no exception being thrown
		setExpression(NullLiteral.INSTANCE);
	}
}