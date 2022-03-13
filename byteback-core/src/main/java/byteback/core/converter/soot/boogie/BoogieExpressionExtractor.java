package byteback.core.converter.soot.boogie;

import java.util.Optional;

import byteback.core.representation.body.soot.SootExpressionVisitor;
import byteback.core.representation.unit.soot.SootMethodUnit;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.AdditionOperation;
import byteback.frontend.boogie.ast.DivisionOperation;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.ModuloOperation;
import byteback.frontend.boogie.ast.MultiplicationOperation;
import byteback.frontend.boogie.ast.NumberLiteral;
import byteback.frontend.boogie.ast.SubtractionOperation;
import byteback.frontend.boogie.ast.ValueReference;
import soot.Local;
import soot.SootMethod;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.DivExpr;
import soot.jimple.IntConstant;
import soot.jimple.MulExpr;
import soot.jimple.RemExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.SubExpr;

public class BoogieExpressionExtractor extends SootExpressionVisitor {

    protected Optional<Expression> expression;

    public BoogieExpressionExtractor() {
        this.expression = Optional.empty();
    }

    public void setExpression(final Expression expression) {
        this.expression = Optional.of(expression);
    }

    public BoogieExpressionExtractor instance() {
        return new BoogieExpressionExtractor();
    }

    @Override
    public void caseStaticInvokeExpr(final StaticInvokeExpr invocation) {
        final SootMethod method = invocation.getMethod();
        final SootMethodUnit methodUnit = new SootMethodUnit(method);
        final FunctionReference functionReference = new FunctionReference();
        functionReference.setAccessor(new Accessor(BoogieNameConverter.methodName(methodUnit)));

        for (Value argument : invocation.getArgs()) {
            BoogieExpressionExtractor extractor = instance();
            argument.apply(extractor);
            functionReference.addArgument(extractor.getResult());
        }

        setExpression(functionReference);
    }

    @Override
    public void caseAddExpr(final AddExpr addition) {
        final BoogieExpressionExtractor leftExtractor = instance();
        final BoogieExpressionExtractor rightExtractor = instance();
        addition.getOp1().apply(leftExtractor);
        addition.getOp2().apply(rightExtractor);
        setExpression(new AdditionOperation(leftExtractor.getResult(), rightExtractor.getResult()));
    }

    @Override
    public void caseSubExpr(final SubExpr subtraction) {
        final BoogieExpressionExtractor leftExtractor = instance();
        final BoogieExpressionExtractor rightExtractor = instance();
        subtraction.getOp1().apply(leftExtractor);
        subtraction.getOp2().apply(rightExtractor);
        setExpression(new SubtractionOperation(leftExtractor.getResult(), rightExtractor.getResult()));
    }

    @Override
    public void caseDivExpr(final DivExpr division) {
        final BoogieExpressionExtractor leftExtractor = instance();
        final BoogieExpressionExtractor rightExtractor = instance();
        division.getOp1().apply(leftExtractor);
        division.getOp2().apply(rightExtractor);
        setExpression(new DivisionOperation(leftExtractor.getResult(), rightExtractor.getResult()));
    }

    @Override
    public void caseMulExpr(final MulExpr multiplication) {
        final BoogieExpressionExtractor leftExtractor = instance();
        final BoogieExpressionExtractor rightExtractor = instance();
        multiplication.getOp1().apply(leftExtractor);
        multiplication.getOp2().apply(rightExtractor);
        setExpression(new MultiplicationOperation(leftExtractor.getResult(), rightExtractor.getResult()));
    }

    @Override
    public void caseRemExpr(final RemExpr modulo) {
        final BoogieExpressionExtractor leftExtractor = instance();
        final BoogieExpressionExtractor rightExtractor = instance();
        modulo.getOp1().apply(leftExtractor);
        modulo.getOp2().apply(rightExtractor);
        setExpression(new ModuloOperation(leftExtractor.getResult(), rightExtractor.getResult()));
    }

    @Override
    public void caseIntConstant(final IntConstant intConstant) {
        setExpression(new NumberLiteral(String.valueOf(intConstant.value)));
    }

    @Override
    public void caseLocal(final Local local) {
        setExpression(new ValueReference(new Accessor(local.getName())));
    }

    @Override
    public void caseDefault(final Value expression) {
        throw new UnsupportedOperationException("Unable to convert Jimple expression of type " + expression.getClass() + " to Boogie");
    }

    @Override
    public Expression getResult() {
        return expression.orElseThrow(() -> {
            throw new IllegalStateException("Cannot retrieve resulting value");
        });
    }

}
