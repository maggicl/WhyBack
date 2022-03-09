package byteback.core.converter.sootboogie;

import java.util.HashMap;
import java.util.Map;

import byteback.core.representation.soot.SootMethodRepresentation;
import byteback.core.visitor.expression.soot.SootExpressionVisitor;
import byteback.core.visitor.type.soot.SootTypeVisitor;
import byteback.core.visitor.unit.soot.SootUnitVisitor;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import soot.BooleanType;
import soot.Local;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.JastAddJ.ReturnStmt;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.StaticInvokeExpr;

public class PredicateConverter {

    private static class ExpressionInliner extends SootUnitVisitor {

        private static class StaticInvocationExtractor extends SootExpressionVisitor {

            private StaticInvokeExpr result;

            @Override
            public void caseStaticInvokeExpr(final StaticInvokeExpr expression) {
                result = expression;
            }

            @Override
            public void defaultCase(final Object object) {
                throw new IllegalArgumentException("Invoke instruction does not target a static method");
            }

            @Override
            public StaticInvokeExpr getResult() {
                return result;
            }

        }

        private static class BoogieExpressionInliner extends SootExpressionVisitor {

            private Expression result;

            @Override
            public void defaultCase(final Object object) {
                // TODO: Generate Boogie expression and substitute the
                // local references by using the localExpressionIndex
                // map.

                throw new UnsupportedOperationException();
            }

            @Override
            public Expression getResult() {
                return result;
            }

        }

        private final Map<Local, Expression> localExpressionIndex;

        public ExpressionInliner() {
            this.localExpressionIndex = new HashMap<>();
        }

        @Override
        public void caseAssignStmt(final AssignStmt assignment) {
            assignment.getRightOp();
        }

        @Override
        public void caseInvokeStmt(final InvokeStmt invocation) {
            final StaticInvocationExtractor extractor = new StaticInvocationExtractor();
            invocation.getInvokeExpr().apply(extractor);
            final StaticInvokeExpr expression = extractor.getResult();
            // TODO: Check that target is a predicate 
            expression.apply(new BoogieExpressionInliner());
        }

        @Override
        public void caseReturnStmt(final ReturnStmt returns) {
            // TODO: finalize the resulting expression.
        }

        @Override
        public void defaultCase(final Object object) {
            Unit unit = (Unit) object;
            throw new UnsupportedOperationException("Inlining of statement " + unit + " is not supported");
        }

    }

    private final FunctionDeclaration predicateDeclaration;

    public PredicateConverter() {
        this.predicateDeclaration = new FunctionDeclaration();
    }

    public FunctionDeclaration convert(SootMethodRepresentation methodRepresentation) {
        methodRepresentation.getReturnType().apply(new SootTypeVisitor() {

            @Override
            public void caseBooleanType(final BooleanType type) {
                // OK...
            }

            @Override
            public void defaultCase(final Object type) {
                throw new IllegalArgumentException("Predicate must return a boolean value");
            }

        });

        final UnitPatchingChain unitChain = methodRepresentation.getBody().getUnits();
        final ExpressionInliner expressionInliner = new ExpressionInliner();

        System.out.println(methodRepresentation.getBody());

        for (Unit unit : unitChain) {
            unit.apply(expressionInliner);
        }

        return predicateDeclaration;
    }

}
