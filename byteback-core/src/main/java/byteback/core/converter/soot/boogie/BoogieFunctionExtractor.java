package byteback.core.converter.soot.boogie;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import byteback.core.representation.unit.soot.SootMethodProxy;
import byteback.core.representation.body.soot.SootExpressionVisitor;
import byteback.core.representation.body.soot.SootStatementVisitor;
import byteback.frontend.boogie.ast.Declarator;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.FunctionSignature;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.ast.ValueReference;
import soot.*;
import soot.jimple.*;

public class BoogieFunctionExtractor {

    private static class BoogieInlineStatementExtractor extends SootStatementVisitor {

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

            private final Map<Local, Optional<Expression>> localExpressionIndex;

            public BoogieInlineExpressionExtractor(final Map<Local, Optional<Expression>> localExpressionIndex,
                    final Program boogieProgram) {

                super(boogieProgram);
                this.localExpressionIndex = localExpressionIndex;
            }

            @Override
            public BoogieInlineExpressionExtractor instance() {
                return new BoogieInlineExpressionExtractor(localExpressionIndex, program);
            }

            @Override
            public void caseLocal(final Local local) {
                final Optional<Expression> expressionOptional = localExpressionIndex.get(local);
                expressionOptional.ifPresentOrElse((expression) -> {
                    setExpression(expression);
                }, () -> {
                    // TODO: Check that the local has been defined properly.
                    super.caseLocal(local);
                });
            }

        }

        private Expression expression;

        private final Program boogieProgram;

        private final Map<Local, Optional<Expression>> localExpressionIndex;

        public BoogieInlineStatementExtractor(Program boogieProgram) {
            this.localExpressionIndex = new HashMap<>();
            this.boogieProgram = boogieProgram;
        }

        public void setExpression(final Expression expression) {
            this.expression = expression;
        }

        @Override
        public Expression getResult() {
            return expression;
        }

        @Override
        public void caseIdentityStmt(final IdentityStmt identity) {
            final LocalExtractor localExtractor = new LocalExtractor();
            identity.getLeftOp().apply(localExtractor);
            localExpressionIndex.put(localExtractor.getResult(), Optional.empty());
        }

        @Override
        public void caseAssignStmt(final AssignStmt assignment) {
            final LocalExtractor localExtractor = new LocalExtractor();
            final BoogieInlineExpressionExtractor expressionExtractor = new BoogieInlineExpressionExtractor(
                    localExpressionIndex, boogieProgram);
            assignment.getLeftOp().apply(localExtractor);
            assignment.getRightOp().apply(expressionExtractor);
            localExpressionIndex.put(localExtractor.getResult(), Optional.of(expressionExtractor.getResult()));
        }

        @Override
        public void caseReturnStmt(final ReturnStmt returns) {
            final LocalExtractor localExtractor = new LocalExtractor();
            returns.getOp().apply(localExtractor);
            // TODO: Report discarded expansions
            setExpression(localExpressionIndex.get(localExtractor.getResult()).get());
        }

        @Override
        public void caseDefault(final Unit unit) {
            throw new UnsupportedOperationException("Cannot inline statements of type " + unit.getClass());
        }

    }

    private final FunctionDeclaration functionDeclaration;

    private final Program boogieProgram;

    public BoogieFunctionExtractor(Program boogieProgram) {
        this.functionDeclaration = new FunctionDeclaration();
        this.boogieProgram = boogieProgram;
    }

    public FunctionDeclaration convert(final SootMethodProxy methodProxy) {
        // TODO: Create function signature
        final FunctionSignature functionSignature = new FunctionSignature();
        final BoogieInlineStatementExtractor expressionExtractor = new BoogieInlineStatementExtractor(boogieProgram);
        functionDeclaration.setDeclarator(new Declarator(methodProxy.getName()));
        functionDeclaration.setSignature(functionSignature);

        for (Unit unit : methodProxy.getBody().getUnits()) {
            unit.apply(expressionExtractor);
        }

        functionDeclaration.setExpression(expressionExtractor.getResult());

        return functionDeclaration;
    }

}
