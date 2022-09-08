package byteback.core.converter.soottoboogie.method.procedure;

import byteback.core.converter.soottoboogie.*;
import byteback.core.converter.soottoboogie.expression.ExpressionExtractor;
import byteback.core.converter.soottoboogie.expression.ExpressionVisitor;
import byteback.core.converter.soottoboogie.method.MethodConverter;
import byteback.core.converter.soottoboogie.type.ReferenceTypeConverter;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.core.representation.soot.unit.SootMethods;
import byteback.frontend.boogie.ast.AssertStatement;
import byteback.frontend.boogie.ast.AssumeStatement;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.Procedure;
import byteback.frontend.boogie.ast.TargetedCallStatement;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.builder.TargetedCallStatementBuilder;
import java.util.Iterator;
import soot.BooleanType;
import soot.IntType;
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

	public ProcedureExpressionExtractor(final Type type, final ProcedureBodyExtractor bodyExtractor) {
		super(type);
		this.bodyExtractor = bodyExtractor;
	}

	@Override
	public ExpressionVisitor makeExpressionVisitor(final Type type) {
		return new ProcedureExpressionExtractor(type, bodyExtractor);
	}

	public TargetedCallStatement makeCall(final SootMethod method, final Iterable<Value> arguments) {
		final var callBuilder = new TargetedCallStatementBuilder();
		callBuilder.name(MethodConverter.methodName(method));
		callBuilder.arguments(new List<Expression>().addAll(convertArguments(method, arguments)));

		return callBuilder.build();
	}

	public void addCall(final TargetedCallStatement callStatement) {
		final List<ValueReference> targets = new List<ValueReference>();
		final Type type = getType();

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
		final Expression condition = visit(argument, BooleanType.v());
		assert !iterator.hasNext() : "Wrong number of arguments to contract method";

		if (name.equals(Namespace.ASSERTION_NAME)) {
			bodyExtractor.addStatement(new AssertStatement(condition));
		} else if (name.equals(Namespace.ASSUMPTION_NAME)) {
			bodyExtractor.addStatement(new AssumeStatement(condition));
		} else if (name.equals(Namespace.INVARIANT_NAME)) {
			bodyExtractor.addInvariant(condition);
		} else {
			throw new ConversionException("Unknown special method: " + method.getName());
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
			addCall(callStatement);
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
		final SootClass baseType = newExpression.getBaseType().getSootClass();
		final String typeName = ReferenceTypeConverter.typeName(baseType);
		callStatement.addArgument(ValueReference.of(typeName));
		addCall(callStatement);
	}

	@Override
	public void caseNewArrayExpr(final NewArrayExpr arrayExpression) {
		final Procedure arrayProcedure = Prelude.v().getArrayProcedure();
		final TargetedCallStatement callStatement = arrayProcedure.makeTargetedCall();

		arrayExpression.getBaseType().apply(new SootTypeVisitor<>() {

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

		final Expression size = makeExpressionVisitor(IntType.v()).visit(arrayExpression.getSize());
		callStatement.addArgument(size);
		addCall(callStatement);
	}

}
