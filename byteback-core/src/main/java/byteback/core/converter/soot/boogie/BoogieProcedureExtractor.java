package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.frontend.boogie.ast.ProcedureDeclaration;
import soot.Unit;

public class BoogieProcedureExtractor extends SootStatementVisitor<ProcedureDeclaration> {

    @Override
    public void caseDefault(final Unit unit) {
        throw new UnsupportedOperationException("Cannot collect statements of type " + unit.getClass().getName());
    }

}
