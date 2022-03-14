package byteback.core.converter.soot.boogie;

import java.util.Optional;

import byteback.core.representation.body.soot.SootExpression;
import byteback.core.representation.body.soot.SootExpressionVisitor;
import byteback.core.representation.unit.soot.SootAnnotation;
import byteback.core.representation.unit.soot.SootAnnotationElement;
import byteback.core.representation.unit.soot.SootMethodUnit;
import byteback.core.representation.unit.soot.SootAnnotationElement.StringElementExtractor;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.AdditionOperation;
import byteback.frontend.boogie.ast.BinaryExpression;
import byteback.frontend.boogie.ast.DivisionOperation;
import byteback.frontend.boogie.ast.EqualsOperation;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.ModuloOperation;
import byteback.frontend.boogie.ast.MultiplicationOperation;
import byteback.frontend.boogie.ast.NegationOperation;
import byteback.frontend.boogie.ast.NumberLiteral;
import byteback.frontend.boogie.ast.OrOperation;
import byteback.frontend.boogie.ast.RealLiteral;
import byteback.frontend.boogie.ast.SubtractionOperation;
import byteback.frontend.boogie.ast.ValueReference;
import soot.Local;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.BinopExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.EqExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.MulExpr;
import soot.jimple.NegExpr;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.SubExpr;

public class BoogieExpressionExtractor extends SootExpressionVisitor<Expression> {

    protected Expression expression;

    public void setExpression(final Expression expression) {
        this.expression = expression;
    }

    public BoogieExpressionExtractor instance() {
        return new BoogieExpressionExtractor();
    }

    public BinaryExpression binaryExpression(final BinopExpr sootExpression, final BinaryExpression boogieExpression) {
        final SootExpression left = new SootExpression(sootExpression.getOp1());
        final SootExpression right = new SootExpression(sootExpression.getOp2());
        boogieExpression.setLeftOperand(instance().visit(left));
        boogieExpression.setRightOperand(instance().visit(right));

        return boogieExpression;
    }

    public void setFunctionReference(final InvokeExpr invocation, final String methodName) {
        final FunctionReference functionReference = new FunctionReference();
        functionReference.setAccessor(new Accessor(methodName));

        for (Value argument : invocation.getArgs()) {
            final SootExpression sootExpression = new SootExpression(argument);
            functionReference.addArgument(instance().visit(sootExpression));
        }

        setExpression(functionReference);
    }
    
    @Override
    public void caseStaticInvokeExpr(final StaticInvokeExpr invocation) {
        final SootMethodUnit methodUnit = new SootMethodUnit(invocation.getMethod());
        final Optional<SootAnnotation> definedAnnotation = methodUnit
                .getAnnotation("Lbyteback/annotations/Contract$Defined;");
        final Optional<String> definedValue = definedAnnotation.flatMap(SootAnnotation::getValue)
                .flatMap((element) -> Optional.of(new StringElementExtractor().visit(element)));

        setFunctionReference(invocation, definedValue.orElseGet(() -> BoogieNameConverter.methodName(methodUnit)));
    }

    @Override
    public void caseAddExpr(final AddExpr addition) {
        setExpression(binaryExpression(addition, new AdditionOperation()));
    }

    @Override
    public void caseSubExpr(final SubExpr subtraction) {
        setExpression(binaryExpression(subtraction, new SubtractionOperation()));
    }

    @Override
    public void caseDivExpr(final DivExpr division) {
        setExpression(binaryExpression(division, new DivisionOperation()));
    }

    @Override
    public void caseMulExpr(final MulExpr multiplication) {
        setExpression(binaryExpression(multiplication, new MultiplicationOperation()));
    }

    @Override
    public void caseRemExpr(final RemExpr modulo) {
        setExpression(binaryExpression(modulo, new ModuloOperation()));
    }

    @Override
    public void caseNegExpr(final NegExpr negation) {
        final SootExpression operand = new SootExpression(negation.getOp());
        setExpression(new NegationOperation(instance().visit(operand)));
    }

    @Override
    public void caseOrExpr(final OrExpr or) {
        setExpression(binaryExpression(or, new OrOperation()));
    }

    @Override
    public void caseEqExpr(final EqExpr equality) {
        setExpression(binaryExpression(equality, new EqualsOperation()));
    }

    @Override
    public void caseIntConstant(final IntConstant intConstant) {
        setExpression(new NumberLiteral(intConstant.toString()));
    }

    @Override
    public void caseDoubleConstant(final DoubleConstant doubleConstant) {
        setExpression(new RealLiteral(doubleConstant.toString()));
    }

    @Override
    public void caseLocal(final Local local) {
        setExpression(new ValueReference(new Accessor(local.getName())));
    }

    @Override
    public void caseDefault(final Value expression) {
        throw new UnsupportedOperationException(
                "Unable to convert Jimple expression of type " + expression.getClass() + " to Boogie");
    }

    @Override
    public Expression result() {
        if (expression == null) {
            throw new IllegalStateException("Could not retrieve expression");
        } else {
            return expression;
        }
    }

}
