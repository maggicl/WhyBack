package byteback.core.converter.soot.boogie;

import java.util.Optional;
import java.util.function.Supplier;

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
import soot.SootMethod;
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

    public BinaryExpression binaryExpression(final BinopExpr sootExpression, final Supplier<? extends BinaryExpression> supplier) {
        final BinaryExpression boogieExpression = supplier.get();
        final BoogieExpressionExtractor leftExtractor = instance();
        final BoogieExpressionExtractor rightExtractor = instance();
        sootExpression.getOp1().apply(leftExtractor);
        sootExpression.getOp2().apply(rightExtractor);
        boogieExpression.setLeftOperand(leftExtractor.getResult());
        boogieExpression.setRightOperand(rightExtractor.getResult());

        return boogieExpression;
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
        setExpression(binaryExpression(addition, () -> new AdditionOperation()));
    }

    @Override
    public void caseSubExpr(final SubExpr subtraction) {
        setExpression(binaryExpression(subtraction, () -> new SubtractionOperation()));
    }

    @Override
    public void caseDivExpr(final DivExpr division) {
        setExpression(binaryExpression(division, () -> new DivisionOperation()));
    }

    @Override
    public void caseMulExpr(final MulExpr multiplication) {
        setExpression(binaryExpression(multiplication, () -> new MultiplicationOperation()));
    }

    @Override
    public void caseRemExpr(final RemExpr modulo) {
        setExpression(binaryExpression(modulo, () -> new ModuloOperation()));
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
    public Expression getResult() {
        return expression.orElseThrow(() -> {
            throw new IllegalStateException("Cannot retrieve resulting value");
        });
    }

}
