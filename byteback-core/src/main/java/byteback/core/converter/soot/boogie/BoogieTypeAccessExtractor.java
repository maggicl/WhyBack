package byteback.core.converter.soot.boogie;

import java.util.Optional;

import byteback.core.representation.type.soot.SootTypeVisitor;
import byteback.frontend.boogie.ast.BooleanTypeAccess;
import byteback.frontend.boogie.ast.IntegerTypeAccess;
import byteback.frontend.boogie.ast.TypeAccess;
import soot.BooleanType;
import soot.IntType;
import soot.Type;

public class BoogieTypeAccessExtractor extends SootTypeVisitor {

    private Optional<TypeAccess> typeAccess;

    public void setTypeAccess(final TypeAccess typeAccess) {
        this.typeAccess = Optional.of(typeAccess);
    }

    @Override
    public void caseIntType(final IntType integerType) {
        setTypeAccess(new IntegerTypeAccess());
    }

    @Override
    public void caseBooleanType(final BooleanType integerType) {
        setTypeAccess(new BooleanTypeAccess());
    }

    @Override
    public void caseDefault(Type type) {
        throw new UnsupportedOperationException("Cannot extract type access for Soot type " + type);
    }

    @Override
    public TypeAccess getResult() {
        return typeAccess.orElseThrow(() -> {
            throw new IllegalStateException("Cannot retrieve resulting value");
        });
    }

}
