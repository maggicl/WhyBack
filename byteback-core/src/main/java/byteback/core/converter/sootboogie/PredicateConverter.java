package byteback.core.converter.sootboogie;

import byteback.core.representation.soot.SootMethodRepresentation;
import byteback.core.visitor.type.soot.SootTypeVisitor;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import soot.BooleanType;
import soot.Unit;
import soot.UnitPatchingChain;

public class PredicateConverter {

    private final FunctionDeclaration predicateDeclaration;

    public PredicateConverter() {
        this.predicateDeclaration = new FunctionDeclaration();
    }

    public FunctionDeclaration convert(SootMethodRepresentation methodRepresentation) {
        methodRepresentation.getReturnType().apply(new SootTypeVisitor() {

                @Override
                public void caseBooleanType(BooleanType type) {
                }

                @Override
                public void defaultCase(Object type) {
                    throw new IllegalArgumentException("Predicate must return a boolean value.");
                }

            });

        UnitPatchingChain unitChain = methodRepresentation.getBody().getUnits();

        for (Unit unit : unitChain) {
            System.out.println(unit);
        }
        
        return predicateDeclaration;
    }
    
}
