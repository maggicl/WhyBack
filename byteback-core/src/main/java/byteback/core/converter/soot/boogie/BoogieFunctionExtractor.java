package byteback.core.converter.soot.boogie;

import java.util.Map;
import java.util.Optional;

import byteback.core.representation.body.soot.SootExpression;
import byteback.core.representation.type.soot.SootType;
import byteback.frontend.boogie.ast.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import byteback.core.util.CountingMap;
import byteback.core.representation.body.soot.SootExpressionVisitor;
import byteback.core.representation.body.soot.SootStatementVisitor;
import byteback.core.representation.unit.soot.SootMethodUnit;
import byteback.frontend.boogie.builder.FunctionDeclarationBuilder;
import byteback.frontend.boogie.builder.FunctionSignatureBuilder;
import soot.*;
import soot.jimple.*;

public class BoogieFunctionExtractor extends SootStatementVisitor<FunctionDeclaration> {

    private static final Logger log = LoggerFactory.getLogger(BoogieFunctionExtractor.class);

    private static class LocalExtractor extends SootExpressionVisitor<Local> {

        private Local local;

        @Override
        public void caseLocal(final Local local) {
            this.local = local;
        }

        @Override
        public void caseDefault(final Value expression) {
            throw new IllegalArgumentException("Expected local definition, got " + expression);
        }

        @Override
        public Local result() {
            if (local == null) {
                throw new IllegalStateException("Could not retrieve local reference");
            } else {
                return local;
            }
        }

    }

    private final FunctionDeclarationBuilder functionBuilder;

    private final FunctionSignatureBuilder signatureBuilder;

    private final CountingMap<Local, Optional<Expression>> localExpressionIndex;

    public BoogieFunctionExtractor(final FunctionDeclarationBuilder functionBuilder) {
        this.signatureBuilder = new FunctionSignatureBuilder();
        this.functionBuilder = functionBuilder;
        this.localExpressionIndex = new CountingMap<>();
    }

    public BoogieFunctionExtractor() {
        this(new FunctionDeclarationBuilder());
    }

    public void convert(final SootMethodUnit methodUnit) {
        functionBuilder.name(BoogieNameConverter.methodName(methodUnit));
        methodUnit.getBody().apply(this);
    }

    @Override
    public void caseIdentityStmt(final IdentityStmt identity) {
        final SootExpression left = new SootExpression(identity.getLeftOp());
        final Local local = new LocalExtractor().visit(left);
        final SootType type = new SootType(local.getType()); 
        final TypeAccess typeAccess = new BoogieTypeAccessExtractor().visit(type);
        final OptionalBinding binding = new OptionalBinding();
        binding.setDeclarator(new Declarator(local.getName()));
        binding.setTypeAccess(typeAccess);
        signatureBuilder.addInputBinding(binding);
        localExpressionIndex.put(local, Optional.empty());
    }

    @Override
    public void caseAssignStmt(final AssignStmt assignment) {
        final SootExpression left = new SootExpression(assignment.getLeftOp());
        final SootExpression right = new SootExpression(assignment.getRightOp());
        final Local local = new LocalExtractor().visit(left);
        final Expression expression = new BoogieInlineExtractor(localExpressionIndex).visit(right);
        localExpressionIndex.put(local, Optional.of(expression));
    }

    @Override
    public void caseReturnStmt(final ReturnStmt returns) {
        final SootExpression operand = new SootExpression(returns.getOp());
        final Local local = new LocalExtractor().visit(operand);
        final TypeAccess typeAccess = new BoogieTypeAccessExtractor().visit(new SootType(local.getType()));
        final OptionalBinding binding = new OptionalBinding();

        for (Map.Entry<Local, Integer> entry : localExpressionIndex.getAccessCount().entrySet()) {
            if (entry.getValue() == 0) {
                log.warn("Local assignment {} unused in final expansion", entry.getKey());
            }
        }

        binding.setTypeAccess(typeAccess);
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

    @Override
    public FunctionDeclaration result() {
        return functionBuilder.build();
    }

}
