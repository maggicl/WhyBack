package byteback.core.converter.soot.boogie;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import byteback.core.util.CountingMap;
import byteback.core.representation.body.soot.SootExpressionVisitor;
import byteback.core.representation.body.soot.SootStatementVisitor;
import byteback.core.representation.unit.soot.SootMethodUnit;
import byteback.frontend.boogie.ast.Declarator;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.OptionalBinding;
import byteback.frontend.boogie.builder.FunctionDeclarationBuilder;
import byteback.frontend.boogie.builder.FunctionSignatureBuilder;
import soot.*;
import soot.jimple.*;

public class BoogieFunctionExtractor extends SootStatementVisitor {

    private static final Logger log = LoggerFactory.getLogger(BoogieFunctionExtractor.class);

    private static class LocalExtractor extends SootExpressionVisitor {

        private Optional<Local> local;

        public LocalExtractor() {
            this.local = Optional.empty();
        }

        @Override
        public void caseLocal(final Local local) {
            this.local = Optional.of(local);
        }

        @Override
        public void caseDefault(final Value expression) {
            throw new IllegalArgumentException("Expected local definition, got " + expression);
        }

        @Override
        public Local getResult() {
            return local.orElseThrow(() -> {
                throw new IllegalStateException("Cannot retrieve resulting value");
            });
        }

    }

    private static class BoogieInlineExtractor extends BoogieExpressionExtractor {

        private final Map<Local, Optional<Expression>> localExpressionIndex;

        public BoogieInlineExtractor(final Map<Local, Optional<Expression>> localExpressionIndex) {
            this.localExpressionIndex = localExpressionIndex;
        }

        @Override
        public BoogieInlineExtractor instance() {
            return new BoogieInlineExtractor(localExpressionIndex);
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

    private final FunctionDeclarationBuilder functionBuilder;

    private final FunctionSignatureBuilder signatureBuilder;

    private final CountingMap<Local, Optional<Expression>> localExpressionIndex;

    public BoogieFunctionExtractor(final FunctionDeclarationBuilder functionBuilder,
            final FunctionSignatureBuilder signatureBuilder) {
        this.functionBuilder = functionBuilder;
        this.signatureBuilder = signatureBuilder;
        this.localExpressionIndex = new CountingMap<>();
    }

    public BoogieFunctionExtractor() {
        this(new FunctionDeclarationBuilder(), new FunctionSignatureBuilder());
    }

    public void convert(final SootMethodUnit methodUnit) {
        functionBuilder.name(BoogieNameConverter.methodName(methodUnit));
        methodUnit.getBody().apply(this);
    }

    @Override
    public FunctionDeclaration getResult() {
        return functionBuilder.build();
    }

    @Override
    public void caseIdentityStmt(final IdentityStmt identity) {
        final LocalExtractor localExtractor = new LocalExtractor();
        final BoogieTypeAccessExtractor typeAccessExtractor = new BoogieTypeAccessExtractor();
        identity.getLeftOp().apply(localExtractor);
        final Local local = localExtractor.getResult();
        local.getType().apply(typeAccessExtractor);
        final OptionalBinding binding = new OptionalBinding();
        binding.setDeclarator(new Declarator(local.getName()));
        binding.setTypeAccess(typeAccessExtractor.getResult());
        signatureBuilder.addInputBinding(binding);
        localExpressionIndex.put(local, Optional.empty());
    }

    @Override
    public void caseAssignStmt(final AssignStmt assignment) {
        final LocalExtractor localExtractor = new LocalExtractor();
        final BoogieInlineExtractor expressionExtractor = new BoogieInlineExtractor(localExpressionIndex);
        assignment.getLeftOp().apply(localExtractor);
        assignment.getRightOp().apply(expressionExtractor);
        localExpressionIndex.put(localExtractor.getResult(), Optional.of(expressionExtractor.getResult()));
    }

    @Override
    public void caseReturnStmt(final ReturnStmt returns) {
        final BoogieTypeAccessExtractor typeAccessExtractor = new BoogieTypeAccessExtractor();
        final LocalExtractor localExtractor = new LocalExtractor();
        final OptionalBinding binding = new OptionalBinding();
        returns.getOp().apply(localExtractor);
        final Local local = localExtractor.getResult();
        local.getType().apply(typeAccessExtractor);

        for (Map.Entry<Local, Integer> entry : localExpressionIndex.getAccessCount().entrySet()) {
            if (entry.getValue() == 0) {
                log.warn("Local assignment {} unused in final expansion", entry.getKey());
            }
        }

        binding.setTypeAccess(typeAccessExtractor.getResult());
        signatureBuilder.outputBinding(binding);
        functionBuilder.signature(signatureBuilder.build())
                .expression(localExpressionIndex.get(local).orElseThrow(() -> {
                    throw new IllegalArgumentException("Cannot inline final statement " + returns);
                }));
    }

    @Override
    public void caseDefault(final Unit unit) {
        throw new UnsupportedOperationException("Cannot inline statements of type " + unit.getClass());
    }

}
