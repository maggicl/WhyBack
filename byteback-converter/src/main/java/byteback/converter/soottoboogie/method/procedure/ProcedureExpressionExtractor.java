package byteback.converter.soottoboogie.method.procedure;

import byteback.analysis.Namespace;
import byteback.analysis.TypeSwitch;
import byteback.analysis.util.SootMethods;
import byteback.converter.soottoboogie.ConversionException;
import byteback.converter.soottoboogie.Prelude;
import byteback.converter.soottoboogie.expression.ExpressionExtractor;
import byteback.converter.soottoboogie.expression.BaseExpressionExtractor;
import byteback.converter.soottoboogie.method.MethodConverter;
import byteback.converter.soottoboogie.type.ReferenceTypeConverter;
import byteback.frontend.boogie.ast.AssertStatement;
import byteback.frontend.boogie.ast.AssumeStatement;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.Procedure;
import byteback.frontend.boogie.ast.TargetedCallStatement;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.builder.TargetedCallStatementBuilder;
import java.util.Iterator;
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

	public void addContractStatement(final SootMethod method, final Iterable<Value> arguments) {
		final String name = method.getName();
		final Iterator<Value> iterator = arguments.iterator();
		final Value argument = iterator.next();
		final Expression condition = visit(argument);
		assert !iterator.hasNext() : "Wrong number of arguments to contract method";

		switch (name) {
			case Namespace.ASSERTION_NAME -> bodyExtractor.addStatement(new AssertStatement(condition));
			case Namespace.ASSUMPTION_NAME -> bodyExtractor.addStatement(new AssumeStatement(condition));
			case Namespace.INVARIANT_NAME -> throw new RuntimeException("Invariants should have already been expanded");
			default -> throw new ConversionException("Unknown special method: " + method.getName());
		}
	}

	@Override
	public void pushFunctionReference(final SootMethod method, final Iterable<Value> arguments) {
		final SootClass clazz = method.getDeclaringClass();
		final boolean isPure = SootMethods.getAnnotation(method, Namespace.PURE_ANNOTATION).isPresent();

		if (isPure) {
			super.pushFunctionReference(method, arguments);
		} else if (Namespace.isContractClass(clazz)) {
			addContractStatement(method, arguments);
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
