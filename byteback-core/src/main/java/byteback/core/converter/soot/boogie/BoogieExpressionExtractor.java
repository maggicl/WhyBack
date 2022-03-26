package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.annotation.SootAnnotation;
import byteback.core.representation.soot.annotation.SootAnnotationElement.StringElementExtractor;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.core.representation.soot.unit.SootFieldUnit;
import byteback.core.representation.soot.unit.SootMethodUnit;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.AdditionOperation;
import byteback.frontend.boogie.ast.AndOperation;
import byteback.frontend.boogie.ast.BinaryExpression;
import byteback.frontend.boogie.ast.BooleanLiteral;
import byteback.frontend.boogie.ast.DivisionOperation;
import byteback.frontend.boogie.ast.EqualsOperation;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.GreaterThanEqualsOperation;
import byteback.frontend.boogie.ast.GreaterThanOperation;
import byteback.frontend.boogie.ast.LessThanEqualsOperation;
import byteback.frontend.boogie.ast.LessThanOperation;
import byteback.frontend.boogie.ast.ModuloOperation;
import byteback.frontend.boogie.ast.MultiplicationOperation;
import byteback.frontend.boogie.ast.NegationOperation;
import byteback.frontend.boogie.ast.NotEqualsOperation;
import byteback.frontend.boogie.ast.NumberLiteral;
import byteback.frontend.boogie.ast.OrOperation;
import byteback.frontend.boogie.ast.RealLiteral;
import byteback.frontend.boogie.ast.SubtractionOperation;
import byteback.frontend.boogie.ast.ValueReference;
import java.util.Optional;
import java.util.Stack;
import soot.BooleanType;
import soot.Local;
import soot.RefType;
import soot.Type;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.BinopExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.EqExpr;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.LeExpr;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.SubExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;

public class BoogieExpressionExtractor extends SootExpressionVisitor<Expression> {

	protected final Stack<Expression> operands;

	protected final SootType type;

	public BoogieExpressionExtractor(final SootType type) {
		this.operands = new Stack<>();
		this.type = type;
	}

	public void pushExpression(final Expression expression) {
		operands.push(expression);
	}

  public void pushCmpExpression(final BinopExpr cmp) {
    final FunctionReference cmpReference = BoogiePrelude.getCmpReference();
		final SootExpression left = new SootExpression(cmp.getOp1());
		final SootExpression right = new SootExpression(cmp.getOp2());
    cmpReference.addArgument(visit(left));
    cmpReference.addArgument(visit(right));
    pushExpression(cmpReference);
  }

	public void pushBinaryExpression(final BinopExpr sootExpression, final BinaryExpression boogieBinary) {
		final SootExpression left = new SootExpression(sootExpression.getOp1());
		final SootExpression right = new SootExpression(sootExpression.getOp2());
		boogieBinary.setLeftOperand(visit(left));
		boogieBinary.setRightOperand(visit(right));
		pushExpression(boogieBinary);
	}

	public BoogieExpressionExtractor argumentExtractor(final SootType type) {
		return new BoogieExpressionExtractor(type);
	}

	public void pushFunctionReference(final InvokeExpr invocation, final FunctionReference functionReference,
			final Expression... ghostParameters) {

		final SootMethodUnit methodUnit = new SootMethodUnit(invocation.getMethod());
		final Optional<SootAnnotation> definedAnnotation = methodUnit
				.getAnnotation("Lbyteback/annotations/Contract$Prelude;");
		final Optional<String> definedValue = definedAnnotation.flatMap(SootAnnotation::getValue)
				.map((element) -> new StringElementExtractor().visit(element));
		final String methodName = definedValue.orElseGet(() -> BoogieNameConverter.methodName(methodUnit));
		functionReference.setAccessor(new Accessor(methodName));
		functionReference.addArgument(BoogiePrelude.getHeapVariable().getValueReference());

		for (Expression parameter : ghostParameters) {
			functionReference.addArgument(parameter);
		}

		for (Value argument : invocation.getArgs()) {
			final SootExpression expression = new SootExpression(argument);
			final SootType type = new SootType(argument.getType());
			functionReference.addArgument(argumentExtractor(type).visit(expression));
		}

		pushExpression(functionReference);
	}

	@Override
	public void caseStaticInvokeExpr(final StaticInvokeExpr invocation) {
		final FunctionReference functionReference = new FunctionReference();
		pushFunctionReference(invocation, functionReference);
	}

	@Override
	public void caseVirtualInvokeExpr(final VirtualInvokeExpr invocation) {
		final FunctionReference functionReference = new FunctionReference();
		final SootExpression base = new SootExpression(invocation.getBase());
		final Expression target = new BoogieExpressionExtractor(new SootType(RefType.v())).visit(base);
		pushFunctionReference(invocation, functionReference, target);
	}

	@Override
	public void caseAddExpr(final AddExpr addition) {
		pushBinaryExpression(addition, new AdditionOperation());
	}

	@Override
	public void caseSubExpr(final SubExpr subtraction) {
		pushBinaryExpression(subtraction, new SubtractionOperation());
	}

	@Override
	public void caseDivExpr(final DivExpr division) {
		pushBinaryExpression(division, new DivisionOperation());
	}

	@Override
	public void caseMulExpr(final MulExpr multiplication) {
		pushBinaryExpression(multiplication, new MultiplicationOperation());
	}

	@Override
	public void caseRemExpr(final RemExpr modulo) {
		pushBinaryExpression(modulo, new ModuloOperation());
	}

	@Override
	public void caseNegExpr(final NegExpr negation) {
		final SootExpression operand = new SootExpression(negation.getOp());
		pushExpression(new NegationOperation(visit(operand)));
	}

	@Override
	public void caseOrExpr(final OrExpr or) {
		type.apply(new SootTypeVisitor<>() {

			@Override
			public void caseBooleanType(final BooleanType type) {
				pushBinaryExpression(or, new OrOperation());
			}

			@Override
			public void caseDefault(final Type type) {
				throw new IllegalArgumentException("Bitwise OR is currently not supported for type " + type);
			}

		});
	}

	@Override
	public void caseAndExpr(final AndExpr and) {
		type.apply(new SootTypeVisitor<>() {

			@Override
			public void caseBooleanType(final BooleanType type) {
				pushBinaryExpression(and, new AndOperation());
			}

			@Override
			public void caseDefault(final Type type) {
				throw new IllegalArgumentException("Bitwise AND is currently not supported for type " + type);
			}

		});
	}

	@Override
	public void caseXorExpr(final XorExpr xor) {
		type.apply(new SootTypeVisitor<>() {

			@Override
			public void caseBooleanType(final BooleanType type) {
				pushBinaryExpression(xor, new NotEqualsOperation());
			}

			@Override
			public void caseDefault(final Type type) {
				throw new IllegalArgumentException("Bitwise XOR is currently not supported for type " + type);
			}

		});
	}

  @Override
  public void caseCmplExpr(final CmplExpr cmpl) {
    pushCmpExpression(cmpl);
  }

  @Override
  public void caseCmpgExpr(final CmpgExpr cmpg) {
    pushCmpExpression(cmpg);
  }

	@Override
	public void caseEqExpr(final EqExpr equals) {
		pushBinaryExpression(equals, new EqualsOperation());
	}

	@Override
	public void caseNeExpr(final NeExpr notEquals) {
		pushBinaryExpression(notEquals, new NotEqualsOperation());
	}

	@Override
	public void caseGtExpr(final GtExpr greaterThan) {
		pushBinaryExpression(greaterThan, new GreaterThanOperation());
	}

	@Override
	public void caseGeExpr(final GeExpr greaterEquals) {
		pushBinaryExpression(greaterEquals, new GreaterThanEqualsOperation());
	}

	@Override
	public void caseLtExpr(final LtExpr lessThan) {
		pushBinaryExpression(lessThan, new LessThanOperation());
	}

	@Override
	public void caseLeExpr(final LeExpr lessEquals) {
		pushBinaryExpression(lessEquals, new LessThanEqualsOperation());
	}

	@Override
	public void caseIntConstant(final IntConstant intConstant) {
		type.apply(new SootTypeVisitor<>() {

			@Override
			public void caseBooleanType(final BooleanType type) {
				pushExpression(intConstant.value != 0 ? BooleanLiteral.getTrue() : BooleanLiteral.getFalse());
			}

			@Override
			public void caseDefault(final Type type) {
				pushExpression(new NumberLiteral(intConstant.toString()));
			}

		});
	}

	@Override
	public void caseDoubleConstant(final DoubleConstant doubleConstant) {
		pushExpression(new RealLiteral(doubleConstant.toString()));
	}

	@Override
	public void caseFloatConstant(final FloatConstant floatConstant) {
		pushExpression(new RealLiteral(floatConstant.toString()));
	}

	@Override
	public void caseNullConstant(final NullConstant nullConstant) {
		pushExpression(BoogiePrelude.getNullConstant().getValueReference());
	}

	@Override
	public void caseLocal(final Local local) {
		pushExpression(new ValueReference(new Accessor(local.getName())));
	}

	@Override
	public void caseInstanceFieldRef(final InstanceFieldRef instanceFieldReference) {
		final SootFieldUnit field = new SootFieldUnit(instanceFieldReference.getField());
		final SootExpression base = new SootExpression(instanceFieldReference.getBase());
		final Expression boogieBase = visit(base);
		final Expression boogieFieldReference = new ValueReference(new Accessor(BoogieNameConverter.fieldName(field)));
		pushExpression(BoogiePrelude.getHeapAccessExpression(boogieBase, boogieFieldReference));
	}

	@Override
	public void caseDefault(final Value expression) {
		throw new UnsupportedOperationException(
				"Unable to convert Jimple expression of type " + expression.getClass() + " to Boogie");
	}

	@Override
	public Expression result() {
		assert operands.size() == 1;

		if (operands.isEmpty()) {
			throw new IllegalStateException("Could not retrieve expression");
		} else {
			return operands.pop();
		}
	}

}
