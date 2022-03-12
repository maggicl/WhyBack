package byteback.core.converter.soot.boogie;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import byteback.core.representation.unit.soot.SootMethodUnit;
import byteback.core.util.CountingMap;
import byteback.core.representation.body.soot.SootExpressionVisitor;
import byteback.core.representation.body.soot.SootStatementVisitor;
import byteback.frontend.boogie.ast.Declarator;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.FunctionSignature;
import byteback.frontend.boogie.ast.Opt;
import byteback.frontend.boogie.ast.OptionalBinding;
import byteback.frontend.boogie.ast.Program;
import soot.*;
import soot.jimple.*;

public class BoogieFunctionConverter {

    private static final Logger log = LoggerFactory.getLogger(BoogieFunctionConverter.class);

    private static class LocalExtractor extends SootExpressionVisitor {

        private Local local;

        @Override
        public void caseLocal(final Local local) {
            this.local = local;
        }

        @Override
        public void caseDefault(final Expr expression) {
            throw new IllegalArgumentException("Expected local definition, got " + expression);
        }

        @Override
        public Local getResult() {
            return local;
        }

    }

    private static class BoogieInlineExpressionExtractor extends BoogieExpressionExtractor {

        private final Map<Local, Optional<Expression>> localExpressionIndex;

        public BoogieInlineExpressionExtractor(final Map<Local, Optional<Expression>> localExpressionIndex,
                final Program program) {

            super(program);
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
                super.caseLocal(local);
            });
        }

    }

    private class BoogieFunctionExpressionExtractor extends SootStatementVisitor {

        private Expression expression;

        private final CountingMap<Local, Optional<Expression>> localExpressionIndex;

        private final Program program;

        public BoogieFunctionExpressionExtractor(final Program program) {
            this.localExpressionIndex = new CountingMap<>();
            this.program = program;
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
                    localExpressionIndex, program);
            assignment.getLeftOp().apply(localExtractor);
            assignment.getRightOp().apply(expressionExtractor);
            localExpressionIndex.put(localExtractor.getResult(), Optional.of(expressionExtractor.getResult()));
        }

        @Override
        public void caseReturnStmt(final ReturnStmt returns) {
            final LocalExtractor localExtractor = new LocalExtractor();
            returns.getOp().apply(localExtractor);

            for (Map.Entry<Local, Integer> entry : localExpressionIndex.getAccessCount().entrySet()) {
                if (entry.getValue() == 0) {
                    log.warn("Local assignment {} unused in the final expansion", entry.getKey());
                }
            }

            setExpression(localExpressionIndex.get(localExtractor.getResult()).get());
        }

        @Override
        public void caseDefault(final Unit unit) {
            throw new UnsupportedOperationException("Cannot inline statements of type " + unit.getClass());
        }

    }

    private final Program program;

    public BoogieFunctionConverter(Program program) {
        this.program = program;
    }

    public FunctionDeclaration convert(final SootMethodUnit methodProxy) {
        final FunctionDeclaration functionDeclaration = new FunctionDeclaration();
        final BoogieTypeAccessExtractor typeAccessExtractor = new BoogieTypeAccessExtractor(program);
        final FunctionSignature functionSignature = new FunctionSignature();
        final BoogieFunctionExpressionExtractor expressionExtractor = new BoogieFunctionExpressionExtractor(program) {

            @Override
            public void caseIdentityStmt(final IdentityStmt identity) {
                final BoogieTypeAccessExtractor typeAccessExtractor = new BoogieTypeAccessExtractor(program);
                final LocalExtractor localExtractor = new LocalExtractor();
                identity.getLeftOp().apply(localExtractor);
                localExtractor.getResult().getType().apply(typeAccessExtractor);
                final Declarator argumentDeclarator = new Declarator(localExtractor.getResult().getName());
                final OptionalBinding argumentBinding = new OptionalBinding();
                argumentBinding.setDeclarator(argumentDeclarator);
                argumentBinding.setTypeAccess(typeAccessExtractor.getResult());
                functionSignature.addInputBinding(argumentBinding);
                super.caseIdentityStmt(identity);
            }

        };

        methodProxy.getReturnType().apply(typeAccessExtractor);
        functionSignature.setOutputBinding(new OptionalBinding(typeAccessExtractor.getResult(), new Opt<>()));
        functionDeclaration.setDeclarator(new Declarator(BoogieNameConverter.convertMethod(methodProxy)));
        functionDeclaration.setSignature(functionSignature);

        for (Unit unit : methodProxy.getBody().getUnits()) {
            unit.apply(expressionExtractor);
        }

        functionDeclaration.setExpression(expressionExtractor.getResult());

        return functionDeclaration;
    }

}