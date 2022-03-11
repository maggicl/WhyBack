package byteback.core.converter.soot.boogie;

import byteback.core.representation.type.soot.SootTypeVisitor;
import byteback.frontend.boogie.ast.BooleanTypeAccess;
import byteback.frontend.boogie.ast.IntegerTypeAccess;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.ast.TypeAccess;
import soot.BooleanType;
import soot.IntType;
import soot.Type;

public class BoogieTypeAccessExtractor extends SootTypeVisitor {

    private TypeAccess typeAccess;

    private final Program program;

    public BoogieTypeAccessExtractor(final Program program) {
        this.program = program;
    }

    public void setTypeAccess(final TypeAccess typeAccess) {
        this.typeAccess = typeAccess;
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
        return typeAccess;
    }

}
