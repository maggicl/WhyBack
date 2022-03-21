package byteback.core.converter.soot.boogie;

import java.util.Optional;
import java.util.Map.Entry;

import byteback.core.converter.soot.SootLocalExtractor;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.type.SootType;
import byteback.frontend.boogie.ast.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import byteback.core.util.CountingMap;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.unit.SootMethodUnit;
import byteback.frontend.boogie.builder.FunctionDeclarationBuilder;
import byteback.frontend.boogie.builder.FunctionSignatureBuilder;
import soot.*;
import soot.jimple.*;

public class BoogieFunctionExtractor extends SootStatementVisitor<FunctionDeclaration> {

    private static final Logger log = LoggerFactory.getLogger(BoogieFunctionExtractor.class);

    private final SootMethodUnit methodUnit;

    private final FunctionDeclarationBuilder functionBuilder;

    private final FunctionSignatureBuilder signatureBuilder;

    private final CountingMap<Local, Optional<Expression>> localExpressionIndex;

    public BoogieFunctionExtractor(final SootMethodUnit methodUnit) {
        this.methodUnit = methodUnit;
        this.functionBuilder = new FunctionDeclarationBuilder();
        this.signatureBuilder = new FunctionSignatureBuilder();
        this.localExpressionIndex = new CountingMap<>();
    }

    public FunctionDeclaration convert() {
        signatureBuilder.addInputBinding(BoogiePrelude.getHeapVariable().makeOptionalBinding());
        functionBuilder.name(BoogieNameConverter.methodName(methodUnit));
        methodUnit.getBody().apply(this);

        return result();
    }

    @Override
    public void caseIdentityStmt(final IdentityStmt identity) {
        final SootExpression left = new SootExpression(identity.getLeftOp());
        final Local local = new SootLocalExtractor().visit(left);
        final SootType type = new SootType(local.getType());
        final TypeAccess boogieTypeAccess = new BoogieTypeAccessExtractor().visit(type);
        final OptionalBinding boogieBinding = new OptionalBinding();
        boogieBinding.setDeclarator(new Declarator(local.getName()));
        boogieBinding.setTypeAccess(boogieTypeAccess);
        signatureBuilder.addInputBinding(boogieBinding);
        localExpressionIndex.put(local, Optional.empty());
    }

    @Override
    public void caseAssignStmt(final AssignStmt assignment) {
        final SootExpression left = new SootExpression(assignment.getLeftOp());
        final SootExpression right = new SootExpression(assignment.getRightOp());
        final Local local = new SootLocalExtractor().visit(left);
        final SootType localType = new SootType(local.getType());
        final Expression boogieExpression = new BoogieInlineExtractor(localType, localExpressionIndex).visit(right);
        localExpressionIndex.put(local, Optional.of(boogieExpression));
    }

    @Override
    public void caseReturnStmt(final ReturnStmt returns) {
        final SootExpression operand = new SootExpression(returns.getOp());
        final SootType returnType = methodUnit.getReturnType();
        final Expression boogieExpression = new BoogieInlineExtractor(returnType, localExpressionIndex).visit(operand);
        final TypeAccess boogieReturnTypeAccess = new BoogieTypeAccessExtractor().visit(returnType);
        final OptionalBinding boogieReturnBinding = new OptionalBinding();

        for (Entry<Local, Integer> entry : localExpressionIndex.getAccessCount().entrySet()) {
            if (entry.getValue() == 0) {
                log.warn("Local assignment {} unused in final expansion", entry.getKey());
            }
        }

        boogieReturnBinding.setTypeAccess(boogieReturnTypeAccess);
        signatureBuilder.outputBinding(boogieReturnBinding);
        functionBuilder.signature(signatureBuilder.build()).expression(boogieExpression);
    }

    @Override
    public void caseDefault(final Unit unit) {
        throw new UnsupportedOperationException("Cannot inline statements of type " + unit.getClass().getName());
    }

    @Override
    public FunctionDeclaration result() {
        return functionBuilder.build();
    }

}
