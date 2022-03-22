package byteback.core.converter.soot.boogie;

import java.util.HashSet;
import java.util.Set;

import byteback.core.converter.soot.SootLocalExtractor;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.Assignee;
import byteback.frontend.boogie.ast.AssignmentStatement;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.BoundedBinding;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.ProcedureDeclaration;
import byteback.frontend.boogie.ast.ReturnStatement;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.ast.VariableDeclaration;
import byteback.frontend.boogie.builder.BoundedBindingBuilder;
import byteback.frontend.boogie.builder.ProcedureDeclarationBuilder;
import byteback.frontend.boogie.builder.ProcedureSignatureBuilder;
import byteback.frontend.boogie.builder.VariableDeclarationBuilder;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;

public class BoogieProcedureExtractor extends SootStatementVisitor<ProcedureDeclaration> {

    private final ProcedureDeclarationBuilder procedureBuilder;

    private final ProcedureSignatureBuilder signatureBuilder;

    private final Body body;

    private final Set<Local> initialized = new HashSet<>(); 

    public BoogieProcedureExtractor(final ProcedureDeclarationBuilder procedureBuilder,
            final ProcedureSignatureBuilder signatureBuilder) {

        this.procedureBuilder = procedureBuilder;
        this.signatureBuilder = signatureBuilder;
        this.body = new Body();
    }

    public void addStatement(final Statement statement) {
        body.addStatement(statement);
    }

    public void addSingleAssignment(final Assignee assignee, final Expression expression) {
        addStatement(new AssignmentStatement(new List<>(assignee), new List<>(expression)));
    }

    public void addReturnStatement() {
        addStatement(new ReturnStatement());
    }

    public void addLocal(final Local local) {
        final SootType type = new SootType(local.getType());
        final TypeAccess typeAccess = new BoogieTypeAccessExtractor().visit(type);
        final BoundedBinding boundedBinding = new BoundedBindingBuilder().addName(local.getName())
                .typeAccess(typeAccess).build();
        final VariableDeclaration variableDeclaration = new VariableDeclarationBuilder().addBinding(boundedBinding)
                .build();

        body.addLocalDeclaration(variableDeclaration);
        initialized.add(local);
    }

    @Override
    public void caseIdentityStmt(final IdentityStmt identity) {
        final SootExpression left = new SootExpression(identity.getLeftOp());
        final Local local = new SootLocalExtractor().visit(left);
        addLocal(local);
    }

    @Override
    public void caseAssignStmt(final AssignStmt assignment) {
        final SootExpression left = new SootExpression(assignment.getLeftOp());
        final SootExpression right = new SootExpression(assignment.getRightOp());

        left.apply(new SootExpressionVisitor<>() {

            @Override
            public void caseLocal(final Local local) {
                final SootType type = new SootType(local.getType());
                final Assignee assignee = new Assignee();
                final Expression expression = new BoogieExpressionExtractor(type).visit(right);
                assignee.setReference(new ValueReference(new Accessor(local.getName())));

                if (!initialized.contains(local)) {
                    addLocal(local);
                }

                addSingleAssignment(assignee, expression);
            }

            @Override
            public void caseDefault(final Value expression) {
                throw new IllegalArgumentException("Conversion for assignee of type " + expression.getClass().getName()
                        + " is currently not supported");
            }

        });

    }

    @Override
    public void caseReturnVoidStmt(final ReturnVoidStmt returns) {
        addReturnStatement();
    }

    @Override
    public void caseReturnStmt(final ReturnStmt returns) {
        final SootType type = new SootType(returns.getOp().getType());
        final SootExpression operand = new SootExpression(returns.getOp());
        final ValueReference valueReference = BoogiePrelude.getReturnValueReference();
        final Assignee assignee = new Assignee(valueReference);
        final Expression expression = new BoogieExpressionExtractor(type).visit(operand);
        addSingleAssignment(assignee, expression);
        addReturnStatement();
    }

    @Override
    public void caseDefault(final Unit unit) {
        throw new UnsupportedOperationException("Cannot collect statements of type " + unit.getClass().getName());
    }

    @Override
    public ProcedureDeclaration result() {
        return procedureBuilder.signature(signatureBuilder.build()).build();
    }

}
