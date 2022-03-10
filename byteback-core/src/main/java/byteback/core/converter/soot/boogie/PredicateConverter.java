package byteback.core.converter.soot.boogie;

import java.util.HashMap;
import java.util.Map;

import byteback.core.representation.clazz.soot.SootMethodProxy;
import byteback.core.representation.body.soot.SootExpressionVisitor;
import byteback.core.representation.type.soot.SootTypeVisitor;
import byteback.core.representation.body.soot.SootUnitVisitor;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.Program;
import soot.*;
import soot.jimple.*;

public class PredicateConverter {

    private static class ExpressionInliner extends SootUnitVisitor {

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
            public void caseDefault(final Expr expression) {
                throw new UnsupportedOperationException("Unable to convert Jimple expression of type " + expression.getClass() + " to Boogie");
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
        public void caseDefault(final Unit unit) {
            System.out.println("Unit: " + unit + " of type " + unit.getClass());
        }

    }

    private final FunctionDeclaration predicateDeclaration;

    private final Program boogieProgram;

    public PredicateConverter(Program boogieProgram) {
        this.predicateDeclaration = new FunctionDeclaration();
        this.boogieProgram = boogieProgram;
    }

    public FunctionDeclaration convert(final SootMethodProxy methodProxy) {
        methodProxy.getSootMethod().getReturnType().apply(new SootTypeVisitor() {

            @Override
            public void caseBooleanType(final BooleanType type) {
                final UnitPatchingChain unitChain = methodProxy.getBody().getUnits();
                final ExpressionInliner expressionInliner = new ExpressionInliner(boogieProgram);

                for (Unit unit : unitChain) {
                    unit.apply(expressionInliner);
                }
            }

            @Override
            public void caseDefault(final Type type) {
                throw new IllegalArgumentException("Predicate must return a boolean value");
            }

        });

        return predicateDeclaration;
    }

}
