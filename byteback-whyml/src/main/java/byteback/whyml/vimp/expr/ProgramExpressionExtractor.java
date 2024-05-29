package byteback.whyml.vimp.expr;

import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.FunctionCall;
import byteback.whyml.syntax.expr.NewArrayExpression;
import byteback.whyml.syntax.expr.NewExpression;
import byteback.whyml.syntax.expr.field.ArrayOperation;
import byteback.whyml.syntax.expr.field.Operation;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyReference;
import byteback.whyml.syntax.type.WhyType;
import byteback.whyml.vimp.TypeResolver;
import byteback.whyml.vimp.VimpFieldParser;
import byteback.whyml.vimp.VimpMethodNameParser;
import byteback.whyml.vimp.VimpMethodParser;
import java.util.List;
import soot.SootMethod;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;

public class ProgramExpressionExtractor extends ProgramLogicalExpressionExtractor {

	public ProgramExpressionExtractor(VimpMethodParser methodSignatureParser,
									  VimpMethodNameParser methodNameParser,
									  TypeResolver typeResolver,
									  VimpFieldParser fieldParser,
									  IdentifierEscaper identifierEscaper) {
		super(fieldParser, typeResolver, methodSignatureParser, methodNameParser, identifierEscaper);
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
	protected boolean isCastPure() {
		return false;
	}

	@Override
	protected Expression parseSpecialClassMethod(InvokeExpr call, List<Expression> argExpressions) {
		throw new WhyTranslationException(call, "special class method '%s' called in program code".formatted(call));
	}

	@Override
	public void caseNewArrayExpr(NewArrayExpr v) {
		final WhyType t = typeResolver.resolveType(v.getBaseType());
		final Expression size = visit(v.getSize());

		if (size.type() != WhyJVMType.INT) {
			throw new WhyTranslationException(v, "array size must not of type INT: " + size.type());
		}

		setExpression(new NewArrayExpression(t, size));
	}

	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		// TODO: implement MULTIANEWARRAY
		throw new WhyTranslationException(v, "NewMultiArrayExpr not supported");
	}

	@Override
	public void caseNewExpr(NewExpr v) {
		final WhyType t = typeResolver.resolveType(v.getType());
		if (t instanceof WhyReference ref) {
			setExpression(new NewExpression(ref));
		} else {
			throw new WhyTranslationException(v, "NewExpr has non-reference type: " + t);
		}
	}

	protected Expression parseMethodCall(InvokeExpr call, List<Expression> argExpressions) {
		final SootMethod method = call.getMethod();

		final WhyFunctionSignature sig = VimpMethodParser.declaration(method)
				// TODO: check if allowing spec methods from program methods is correct. Needed because some
				//  implementation methods are annotated as @Pure in order to define a spec
//				.filter(WhyFunctionDeclaration::isProgram)
				.map(decl -> methodSignatureParser.signature(method, decl))
				.orElseThrow(() -> new WhyTranslationException(call,
						"method '%s' is not callable from a program expression".formatted(method)));

		return FunctionCall.build(methodNameParser.methodName(sig), sig, argExpressions);
	}
}