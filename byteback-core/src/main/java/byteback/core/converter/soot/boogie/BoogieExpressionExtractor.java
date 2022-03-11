package byteback.core.converter.soot.boogie;

import byteback.core.representation.body.soot.SootExpressionVisitor;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.AdditionOperation;
import byteback.frontend.boogie.ast.DivisionOperation;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.MultiplicationOperation;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.ast.SubtractionOperation;
import byteback.frontend.boogie.ast.ValueReference;
import soot.Local;
import soot.SootMethod;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.DivExpr;
import soot.jimple.Expr;
import soot.jimple.MulExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.SubExpr;

public class BoogieExpressionExtractor extends SootExpressionVisitor {

    private Expression expression;

    private final Program program;

    public BoogieExpressionExtractor(final Program program) {
        this.program = program;
    }

    public void setExpression(final Expression expression) {
        this.expression = expression;
    }

    @Override
    public BoogieExpressionExtractor clone() {
        return new BoogieExpressionExtractor(program);
    }

    @Override
    public void caseStaticInvokeExpr(final StaticInvokeExpr invocation) {
        // TODO: Determine if invocation target is a procedure or a function in the program.
        final SootMethod method = invocation.getMethod();
        final FunctionReference functionReference = new FunctionReference();
        // TODO: Infer method name based on argument types to support overloading.
        functionReference.setAccessor(new Accessor(method.getName()));

        for (Value argument : invocation.getArgs()) {
            BoogieExpressionExtractor extractor = clone();
            argument.apply(extractor);
            functionReference.addArgument(extractor.getResult());
        }

        expression = functionReference;
    }

    @Override
    public void caseAddExpr(final AddExpr addition) {
        final BoogieExpressionExtractor leftExtractor = clone();
        final BoogieExpressionExtractor rightExtractor = clone();
        addition.getOp1().apply(leftExtractor);
        addition.getOp2().apply(rightExtractor);
        setExpression(new AdditionOperation(leftExtractor.getResult(), rightExtractor.getResult()));
    }

    @Override
    public void caseSubExpr(final SubExpr subtraction) {
        final BoogieExpressionExtractor leftExtractor = clone();
        final BoogieExpressionExtractor rightExtractor = clone();
        subtraction.getOp1().apply(leftExtractor);
        subtraction.getOp2().apply(rightExtractor);
        setExpression(new SubtractionOperation(leftExtractor.getResult(), rightExtractor.getResult()));
    }

    @Override
    public void caseDivExpr(final DivExpr division) {
        final BoogieExpressionExtractor leftExtractor = clone();
        final BoogieExpressionExtractor rightExtractor = clone();
        division.getOp1().apply(leftExtractor);
        division.getOp2().apply(rightExtractor);
        setExpression(new DivisionOperation(leftExtractor.getResult(), rightExtractor.getResult()));
    }

    @Override
    public void caseMulExpr(final MulExpr multiplication) {
        final BoogieExpressionExtractor leftExtractor = clone();
        final BoogieExpressionExtractor rightExtractor = clone();
        multiplication.getOp1().apply(leftExtractor);
        multiplication.getOp2().apply(rightExtractor);
        setExpression(new MultiplicationOperation(leftExtractor.getResult(), rightExtractor.getResult()));
    }

    @Override
    public void caseLocal(Local local) {
        setExpression(new ValueReference(new Accessor(local.getName())));
    }

    @Override
    public void caseDefault(final Expr expression) {
        throw new UnsupportedOperationException("Unable to convert Jimple expression of type " + expression.getClass() + " to Boogie");
    }

    @Override
    public Expression getResult() {
        return expression;
    }

}
