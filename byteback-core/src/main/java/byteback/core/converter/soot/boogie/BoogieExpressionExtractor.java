package byteback.core.converter.soot.boogie;

import java.util.Optional;

import byteback.core.representation.body.soot.SootExpression;
import byteback.core.representation.body.soot.SootExpressionVisitor;
import byteback.core.representation.unit.soot.SootMethodUnit;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.AdditionOperation;
import byteback.frontend.boogie.ast.BinaryExpression;
import byteback.frontend.boogie.ast.DivisionOperation;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.ModuloOperation;
import byteback.frontend.boogie.ast.MultiplicationOperation;
import byteback.frontend.boogie.ast.NumberLiteral;
import byteback.frontend.boogie.ast.RealLiteral;
import byteback.frontend.boogie.ast.SubtractionOperation;
import byteback.frontend.boogie.ast.ValueReference;
import soot.Local;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.BinopExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.IntConstant;
import soot.jimple.MulExpr;
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

    @Override
    public void caseStaticInvokeExpr(final StaticInvokeExpr invocation) {
        final SootMethodUnit methodUnit = new SootMethodUnit(invocation.getMethod());
        final FunctionReference functionReference = new FunctionReference();
        functionReference.setAccessor(new Accessor(BoogieNameConverter.methodName(methodUnit)));

        for (Value argument : invocation.getArgs()) {
            final SootExpression sootExpression = new SootExpression(argument);
            functionReference.addArgument(instance().visit(sootExpression));
        }

        setExpression(functionReference);
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
