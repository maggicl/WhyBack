package byteback.whyml.vimp.expr;

import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.LocalExpression;
import byteback.whyml.syntax.expr.NewArrayExpression;
import byteback.whyml.syntax.expr.NewExpression;
import byteback.whyml.syntax.expr.PureFunctionCall;
import byteback.whyml.syntax.expr.field.ArrayOperation;
import byteback.whyml.syntax.expr.field.Operation;
import byteback.whyml.syntax.function.WhyFunctionDeclaration;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyReference;
import byteback.whyml.syntax.type.WhyType;
import byteback.whyml.vimp.TypeResolver;
import byteback.whyml.vimp.VimpFieldParser;
import byteback.whyml.vimp.VimpMethodNameParser;
import byteback.whyml.vimp.VimpMethodParser;
import java.util.List;
import soot.SootMethod;
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
	protected Expression parseSpecialClassMethod(SootMethod method, List<Expression> argExpressions) {
		throw new IllegalStateException("special class method " + method + " called in program code");
	}

	@Override
	protected Expression parsePrimitiveOpMethod(SootMethod method, List<Expression> argExpressions) {
		throw new IllegalStateException("primitive operator method " + method + " called in program code");
	}

	@Override
	protected Expression parseMethodCall(SootMethod method, List<Expression> argExpressions) {
		final WhyFunctionSignature sig = VimpMethodParser.declaration(method)
				.filter(WhyFunctionDeclaration::isProgram)
				.map(decl -> methodSignatureParser.signature(method, decl))
				.orElseThrow(() -> new IllegalStateException("method " + method + " is not callable from a program expression"));

		return new PureFunctionCall(methodNameParser.methodName(sig), sig, argExpressions);
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
}