package byteback.core.converter.soot.boogie;

import byteback.core.converter.soot.SootLocalExtractor;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.frontend.boogie.ast.BoundedBinding;
import byteback.frontend.boogie.ast.Declarator;
import byteback.frontend.boogie.ast.ProcedureDeclaration;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.builder.ProcedureDeclarationBuilder;
import byteback.frontend.boogie.builder.ProcedureSignatureBuilder;

import soot.Local;
import soot.Unit;
import soot.jimple.IdentityStmt;

public class BoogieProcedureExtractor extends SootStatementVisitor<ProcedureDeclaration> {

    private final ProcedureDeclarationBuilder procedureBuilder;

    private final ProcedureSignatureBuilder signatureBuilder;

    public BoogieProcedureExtractor(final ProcedureDeclarationBuilder procedureBuilder,
            final ProcedureSignatureBuilder signatureBuilder) {

        this.procedureBuilder = procedureBuilder;
        this.signatureBuilder = signatureBuilder;
    }

    @Override
    public void caseIdentityStmt(final IdentityStmt identity) {
        final SootExpression left = new SootExpression(identity.getLeftOp());
        final Local local = new SootLocalExtractor().visit(left);
        final SootType type = new SootType(local.getType());
        final TypeAccess boogieTypeAccess = new BoogieTypeAccessExtractor().visit(type);
        final BoundedBinding boogieBinding = new BoundedBinding();
        boogieBinding.addDeclarator(new Declarator(local.getName()));
        boogieBinding.setTypeAccess(boogieTypeAccess);
        signatureBuilder.addInputBinding(boogieBinding);
    }

    @Override
    public void caseDefault(final Unit unit) {
        throw new UnsupportedOperationException("Cannot collect statements of type " + unit.getClass().getName());
    }

}
