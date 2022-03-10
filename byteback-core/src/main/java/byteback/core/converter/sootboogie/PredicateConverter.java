package byteback.core.converter.sootboogie;

import java.util.HashMap;
import java.util.Map;

import byteback.core.representation.soot.SootMethodRepresentation;
import byteback.core.visitor.expression.soot.SootExpressionVisitor;
import byteback.core.visitor.type.soot.SootTypeVisitor;
import byteback.core.visitor.unit.soot.SootUnitVisitor;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.Program;
import soot.BooleanType;
import soot.Local;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticInvokeExpr;

public class PredicateConverter {

    private static class ExpressionInliner extends SootUnitVisitor {

        private static class LocalExtractor extends SootExpressionVisitor {
            private Local result;

            @Override
            public void caseLocal(final Local expression) {
                result = expression;
            }

            @Override
            public void defaultCase(final Object object) {
                throw new IllegalArgumentException("Expected local definition, got " + object);
            }

            @Override
            public Local getResult() {
                return result;
            }
        }

        // This extractor is a more general version of BoogieInlineExpressionExtractor.
        // TODO: Extract this inner class
        private static class BoogieExpressionExtractor extends SootExpressionVisitor {

            @Override
            public void caseParameterRef(final ParameterRef parameterReference) {
                // TODO: Initialize function parameters. 
            }

            @Override
            public void caseStaticInvokeExpr(final StaticInvokeExpr invocation) {
                // TODO: lookup in program to determine if invocation
                // refers to a predicate
                System.out.println("Invocation: " + invocation);
                for (Value value : invocation.getArgs()) {
                    LocalExtractor extractor = new LocalExtractor();
                    value.apply(extractor);
                    Local local = extractor.getResult();
                    System.out.println(local);
                }
            }

            @Override
            public void defaultCase(final Object object) {
                throw new UnsupportedOperationException("Unable to convert Jimple expression of type " + object.getClass() + " to Boogie");
            }
            
        }

        private static class BoogieInlineExpressionExtractor extends BoogieExpressionExtractor {

            private Expression result;

            private Map<Local, Expression> localExpressionIndex;

            private final Program boogieProgram;

            public BoogieInlineExpressionExtractor(Map<Local, Expression> localExpressionIndex, Program boogieProgram) {
                this.localExpressionIndex = localExpressionIndex;
                this.boogieProgram = boogieProgram;
            }

            @Override
            public Expression getResult() {
                return result;
            }

        }

        private final Map<Local, Expression> localExpressionIndex;

        private final Program boogieProgram;

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
        public void defaultCase(final Object object) {
            Unit unit = (Unit) object;
            System.out.println("Unit: " + unit + " of type " + unit.getClass());
        }

    }

    private final FunctionDeclaration predicateDeclaration;

    private final Program boogieProgram;

    public PredicateConverter(Program boogieProgram) {
        this.predicateDeclaration = new FunctionDeclaration();
        this.boogieProgram = boogieProgram;
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
        final ExpressionInliner expressionInliner = new ExpressionInliner(boogieProgram);

        System.out.println(methodRepresentation.getBody());

        for (Unit unit : unitChain) {
            unit.apply(expressionInliner);
        }

        return predicateDeclaration;
    }

}
