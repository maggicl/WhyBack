package byteback.core.converter.soot.boogie;

import byteback.core.representation.type.soot.SootTypeVisitor;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.BooleanTypeAccess;
import byteback.frontend.boogie.ast.IntegerTypeAccess;
import byteback.frontend.boogie.ast.RealTypeAccess;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.UnknownTypeAccess;
import soot.BooleanType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.RefType;
import soot.Type;

public class BoogieTypeAccessExtractor extends SootTypeVisitor<TypeAccess> {

    private TypeAccess typeAccess;

    public void setTypeAccess(final TypeAccess typeAccess) {
        this.typeAccess = typeAccess;
    }

    @Override
    public void caseIntType(final IntType integerType) {
        setTypeAccess(new IntegerTypeAccess());
    }

    @Override
    public void caseDoubleType(final DoubleType doubleType) {
        setTypeAccess(new RealTypeAccess());
    }

    @Override
    public void caseFloatType(final FloatType floatType) {
        setTypeAccess(new RealTypeAccess());
    }

    @Override
    public void caseBooleanType(final BooleanType booleanType) {
        setTypeAccess(new BooleanTypeAccess());
    }

    @Override
    public void caseRefType(final RefType referenceType) {
        final UnknownTypeAccess typeAccess = new UnknownTypeAccess();
        final Accessor accessor = new Accessor("Reference");
        typeAccess.setAccessor(accessor);
        setTypeAccess(typeAccess);
    }

    @Override
    public void caseDefault(Type type) {
        throw new UnsupportedOperationException("Cannot extract type access for Soot type " + type);
    }

    @Override
    public TypeAccess result() {
        if (typeAccess == null) {
            throw new IllegalStateException("Could not retrieve type access");
        } else {
            return typeAccess;
        }
    }

}
