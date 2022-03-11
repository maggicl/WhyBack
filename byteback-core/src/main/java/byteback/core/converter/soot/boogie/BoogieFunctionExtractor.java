package byteback.core.converter.soot.boogie;

import java.util.HashMap;
import java.util.Map;

import byteback.core.representation.unit.soot.SootMethodProxy;
import byteback.core.representation.body.soot.SootExpressionVisitor;
import byteback.core.representation.body.soot.SootStatementVisitor;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.Program;
import soot.*;
import soot.jimple.*;

public class BoogieFunctionExtractor {

    private static class ExpressionInliner extends SootStatementVisitor {

        private static class LocalExtractor extends SootExpressionVisitor {
            private Local result;

            @Override
            public void caseLocal(final Local expression) {
                result = expression;
            }

            @Override
            public void caseDefault(final Expr expression) {
                throw new IllegalArgumentException("Expected local definition, got " + expression);
            }

            @Override
            public Local getResult() {
                return result;
            }
        }

        private static class BoogieInlineExpressionExtractor extends BoogieExpressionExtractor {

            private Map<Local, Expression> localExpressionIndex;

            public BoogieInlineExpressionExtractor(Map<Local, Expression> localExpressionIndex, Program boogieProgram) {
                super(boogieProgram);
                this.localExpressionIndex = localExpressionIndex;
            }

        }

        private final Program boogieProgram;

        private final Map<Local, Expression> localExpressionIndex;

        public ExpressionInliner(Program boogieProgram) {
            this.localExpressionIndex = new HashMap<>();
            this.boogieProgram = boogieProgram;
        }

        @Override
        public void caseIdentityStmt(final IdentityStmt identity) {
            // TODO: Initialize table entry
        }

        @Override
        public void caseAssignStmt(final AssignStmt assignment) {
            final LocalExtractor localExtractor = new LocalExtractor();
            final BoogieInlineExpressionExtractor expressionExtractor = new BoogieInlineExpressionExtractor(
                    localExpressionIndex, boogieProgram);
            assignment.getLeftOp().apply(localExtractor);
            assignment.getRightOp().apply(expressionExtractor);
            final Local left = localExtractor.getResult();
            final Expression right = expressionExtractor.getResult();
            localExpressionIndex.put(left, right);
        }

        @Override
        public void caseReturnStmt(final ReturnStmt returns) {
            // TODO: finalize the resulting expression.
        }

        @Override
        public void caseDefault(final Unit unit) {
            System.out.println("Unit: " + unit + " of type " + unit.getClass());
        }

    }

    private final FunctionDeclaration predicateDeclaration;

    private final Program boogieProgram;

    public BoogieFunctionExtractor(Program boogieProgram) {
        this.predicateDeclaration = new FunctionDeclaration();
        this.boogieProgram = boogieProgram;
    }

    public FunctionDeclaration convert(final SootMethodProxy methodProxy) {
        final UnitPatchingChain unitChain = methodProxy.getBody().getUnits();
        final ExpressionInliner expressionInliner = new ExpressionInliner(boogieProgram);

        for (Unit unit : unitChain) {
            unit.apply(expressionInliner);
        }

        return predicateDeclaration;
    }

}
