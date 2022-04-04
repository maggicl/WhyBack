package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootField;
import byteback.frontend.boogie.ast.Assignee;
import byteback.frontend.boogie.ast.BlockStatement;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.GotoStatement;
import byteback.frontend.boogie.ast.IfStatement;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.ReturnStatement;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.ValueReference;
import java.util.function.Supplier;
import soot.IntType;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;

public class StatementExtractor extends SootStatementVisitor<Statement> {

	private final Body body;

	private final SootType returnType;

	private final LabelCollector labelCollector;

	private final Supplier<ValueReference> referenceSupplier;

  private Statement statement;

	public StatementExtractor(final Body body, final SootType returnType, final LabelCollector labelCollector,
			final Supplier<ValueReference> referenceSupplier) {
		this.body = body;
		this.returnType = returnType;
		this.labelCollector = labelCollector;
		this.referenceSupplier = referenceSupplier;
	}

	public void addStatement(final Statement statement) {
		body.addStatement(statement);
    this.statement = statement;
	}

	public void addSingleAssignment(final Assignee assignee, final Expression expression) {
		addStatement(Prelude.makeSingleAssignment(assignee, expression));
	}

	public BlockStatement makeThenBlock(final Statement statement) {
		final BlockStatement blockStatement = new BlockStatement();
		blockStatement.addStatement(statement);

		return blockStatement;
	}

	@Override
	public void caseAssignStmt(final AssignStmt assignment) {
		final SootExpression left = new SootExpression(assignment.getLeftOp());
		final SootExpression right = new SootExpression(assignment.getRightOp());

		left.apply(new SootExpressionVisitor<>() {

			@Override
			public void caseLocal(final Local local) {
				final ValueReference reference = ValueReference.of(local.getName());
				final var extractor = new ProcedureExpressionExtractor(body, () -> reference);
				final Expression assigned = extractor.visit(right, left.getType());

				if (!assigned.equals(reference)) {
					addSingleAssignment(new Assignee(reference), assigned);
				}
			}

			@Override
			public void caseInstanceFieldRef(final InstanceFieldRef instanceFieldReference) {
				final var field = new SootField(instanceFieldReference.getField());
				final var base = new SootExpression(instanceFieldReference.getBase());
				final var extractor = new ProcedureExpressionExtractor(body, referenceSupplier);
				final Expression assigned = extractor.visit(right, left.getType());
				final Expression fieldReference = ValueReference.of(NameConverter.fieldName(field));
				final Expression boogieBase = new ExpressionExtractor().visit(base);
				addStatement(Prelude.getHeapUpdateStatement(boogieBase, fieldReference, assigned));
			}

			@Override
			public void caseDefault(final Value value) {
				throw new IllegalArgumentException("Unknown left hand side argument in assignment: " + assignment);
			}

		});
	}

	@Override
	public void caseReturnVoidStmt(final ReturnVoidStmt returnStatement) {
		addStatement(new ReturnStatement());
	}

	@Override
	public void caseReturnStmt(final ReturnStmt returnStatement) {
		final SootExpression operand = new SootExpression(returnStatement.getOp());
		final ValueReference valueReference = Prelude.getReturnValueReference();
		final Assignee assignee = new Assignee(valueReference);
		final Expression expression = new ExpressionExtractor().visit(operand, returnType);
		addSingleAssignment(assignee, expression);
		addStatement(new ReturnStatement());
	}

	@Override
	public void caseGotoStmt(final GotoStmt gotoStatement) {
		final Unit targetUnit = gotoStatement.getTarget();
		final Label label = labelCollector.getLabel(targetUnit).get();
		addStatement(new GotoStatement(label));
	}

	@Override
	public void caseIfStmt(final IfStmt ifStatement) {
		final var condition = new SootExpression(ifStatement.getCondition());
		final var type = new SootType(IntType.v());
		final Expression boogieCondition = new ExpressionExtractor().visit(condition, type);
		final IfStatement boogieIfStatement = new IfStatement();
		final Label label = labelCollector.getLabel(ifStatement.getTarget()).get();
		final BlockStatement boogieThenBlock = makeThenBlock(new GotoStatement(label));
		boogieIfStatement.setCondition(boogieCondition);
		boogieIfStatement.setThen(boogieThenBlock);
		addStatement(boogieIfStatement);
	}

	@Override
	public void caseInvokeStmt(final InvokeStmt invokeStatement) {
		final SootExpression invokeExpression = new SootExpression(invokeStatement.getInvokeExpr());
		invokeExpression.apply(new ProcedureExpressionExtractor(body));
	}

	@Override
	public void caseDefault(final Unit unit) {
		throw new IllegalArgumentException("Cannot extract procedure with statement " + unit);
	}

	@Override
	public Statement result() {
		return statement;
	}

}
