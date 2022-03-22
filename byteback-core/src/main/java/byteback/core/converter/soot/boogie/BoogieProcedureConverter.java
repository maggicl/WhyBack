package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.unit.SootMethodUnit;
import byteback.frontend.boogie.ast.ProcedureDeclaration;

public class BoogieProcedureConverter {

    private static final BoogieProcedureConverter instance = new BoogieProcedureConverter();

    public static BoogieProcedureConverter instance() {
        return instance;
    }

    public ProcedureDeclaration convert(final SootMethodUnit methodUnit) {
        return new BoogieProcedureExtractor().visit(methodUnit.getBody());
    }

}
