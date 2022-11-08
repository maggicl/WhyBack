package byteback.converter.soottoboogie.method.procedure;

import byteback.analysis.Namespace;
import byteback.analysis.TypeSwitch;
import byteback.analysis.util.SootMethods;
import byteback.converter.soottoboogie.Prelude;
import byteback.converter.soottoboogie.expression.ExpressionExtractor;
import byteback.converter.soottoboogie.expression.BaseExpressionExtractor;
import byteback.converter.soottoboogie.method.MethodConverter;
import byteback.converter.soottoboogie.type.ReferenceTypeConverter;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.Procedure;
import byteback.frontend.boogie.ast.TargetedCallStatement;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.builder.TargetedCallStatementBuilder;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.UnknownType;
import soot.Value;
import soot.VoidType;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;

public class ProcedureExpressionExtractor extends ExpressionExtractor {

	final ProcedureBodyExtractor bodyExtractor;

	public ProcedureExpressionExtractor(final ProcedureBodyExtractor bodyExtractor) {
		this.bodyExtractor = bodyExtractor;
	}

	@Override
	public BaseExpressionExtractor makeExpressionExtractor() {
		return new ProcedureExpressionExtractor(bodyExtractor);
	}

	public TargetedCallStatement makeCall(final SootMethod method, final Iterable<Value> arguments) {
		final var callBuilder = new TargetedCallStatementBuilder();
		callBuilder.name(MethodConverter.methodName(method));
		callBuilder.arguments(new List<Expression>().addAll(convertArguments(method, arguments)));

		return callBuilder.build();
	}

	public void addCall(final TargetedCallStatement callStatement, final Type type) {
		final List<ValueReference> targets = new List<ValueReference>();

		if (type != VoidType.v() && type != UnknownType.v()) {
			final ValueReference reference = bodyExtractor.generateReference(type);
			targets.add(reference);
			setExpression(reference);
		}

		callStatement.setTargetList(targets);
		bodyExtractor.addStatement(callStatement);
	}

	@Override
	public void pushFunctionReference(final SootMethod method, final Iterable<Value> arguments) {
		final boolean isPure = SootMethods.getAnnotation(method, Namespace.PURE_ANNOTATION).isPresent();

		if (isPure) {
			super.pushFunctionReference(method, arguments);
		} else {
			final TargetedCallStatement callStatement = makeCall(method, arguments);
			addCall(callStatement, method.getReturnType());
		}
	}

	@Override
	public void caseSpecialInvokeExpr(final SpecialInvokeExpr invoke) {
		caseInstanceInvokeExpr(invoke);
	}

	@Override
	public void caseNewExpr(final NewExpr newExpression) {
		final Procedure newProcedure = Prelude.v().getNewProcedure();
		final TargetedCallStatement callStatement = newProcedure.makeTargetedCall();
		final SootClass baseClass = newExpression.getBaseType().getSootClass();
		final String typeName = ReferenceTypeConverter.typeName(baseClass);
		callStatement.addArgument(ValueReference.of(typeName));
		addCall(callStatement, newExpression.getType());
	}

	@Override
	public void caseNewArrayExpr(final NewArrayExpr arrayExpression) {
		final Procedure arrayProcedure = Prelude.v().getArrayProcedure();
		final TargetedCallStatement callStatement = arrayProcedure.makeTargetedCall();

		arrayExpression.getBaseType().apply(new TypeSwitch<>() {

			@Override
			public void caseRefType(final RefType referenceType) {
				final SootClass baseType = referenceType.getSootClass();
				final String typeName = ReferenceTypeConverter.typeName(baseType);
				callStatement.addArgument(ValueReference.of(typeName));
			}

			@Override
			public void caseDefault(final soot.Type type) {
				callStatement.addArgument(Prelude.v().getPrimitiveTypeConstant().makeValueReference());
			}

		});

		final Expression size = this.makeExpressionExtractor().visit(arrayExpression.getSize());
		callStatement.addArgument(size);
		addCall(callStatement, arrayExpression.getType());
	}

}
