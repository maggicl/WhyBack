package byteback.core.converter.soot.boogie;

import byteback.core.converter.soot.boogie.ProcedureBodyExtractor.VariableProvider;
import byteback.core.converter.soot.boogie.ProcedureExpressionExtractor.VariableSupplier;
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
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import java.util.Map;
import java.util.Optional;
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

public class StatementExtractor extends SootStatementVisitor<Optional<Statement>> {

	private final ProcedureBodyExtractor extractor;

	private Statement statement;

	public StatementExtractor(final ProcedureBodyExtractor extractor) {
		this.extractor = extractor;
	}

	public BlockStatement makeThenBlock(final Statement statement) {
		final BlockStatement blockStatement = new BlockStatement();
		blockStatement.addStatement(statement);

		return blockStatement;
	}

	public void addStatement(final Statement statement) {
		this.statement = statement;
		extractor.addStatement(statement);
	}

	public void addInvariant(final Expression expression) {
		extractor.addInvariant(expression);
	}

	public void addSingleAssignment(final Assignee assignee, final Expression expression) {
		addStatement(Prelude.makeSingleAssignment(assignee, expression));
	}

	public Body getBody() {
		return extractor.getBody();
	}

	public SootType getReturnType() {
		return extractor.getReturnType();
	}

	public LoopCollector getLoopCollector() {
		return extractor.getLoopCollector();
	}

	public LabelCollector getLabelCollector() {
		return extractor.getLabelCollector();
	}

	public VariableProvider getVariableProvider() {
		return extractor.getVariableProvider();
	}

	public Map<Local, Optional<Expression>> getExpressionTable() {
		return extractor.getExpressionTable();
	}

	@Override
	public void caseAssignStmt(final AssignStmt assignment) {
		final SootExpression left = new SootExpression(assignment.getLeftOp());
		final SootExpression right = new SootExpression(assignment.getRightOp());

		left.apply(new SootExpressionVisitor<>() {

			@Override
			public void caseLocal(final Local local) {
				final ValueReference reference = ValueReference.of(local.getName());
				final VariableSupplier supplier = () -> reference;
				final var extractor = new ProcedureExpressionExtractor(StatementExtractor.this, supplier);
				final Expression assigned = extractor.visit(right, left.getType());

				if (!assigned.equals(reference)) {
					addSingleAssignment(new Assignee(reference), assigned);
					getExpressionTable().put(local, Optional.of(assigned));
				}
			}

			@Override
			public void caseInstanceFieldRef(final InstanceFieldRef instanceFieldReference) {
				final var field = new SootField(instanceFieldReference.getField());
				final var base = new SootExpression(instanceFieldReference.getBase());
				final VariableSupplier supplier = () -> {
					final TypeAccess typeAccess = new TypeAccessExtractor().visit(field.getType());

					return extractor.getVariableProvider().apply(typeAccess);
				};
				final var extractor = new ProcedureExpressionExtractor(StatementExtractor.this, supplier);
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
		final Expression expression = new ExpressionExtractor().visit(operand, getReturnType());
		addSingleAssignment(assignee, expression);
		addStatement(new ReturnStatement());
	}

	@Override
	public void caseGotoStmt(final GotoStmt gotoStatement) {
		final Unit targetUnit = gotoStatement.getTarget();
		final Label label = getLabelCollector().getLabel(targetUnit).get();
		addStatement(new GotoStatement(label));
	}

	@Override
	public void caseIfStmt(final IfStmt ifStatement) {
		final var condition = new SootExpression(ifStatement.getCondition());
		final var type = new SootType(IntType.v());
		final Expression boogieCondition = new ExpressionExtractor().visit(condition, type);
		final IfStatement boogieIfStatement = new IfStatement();
		final Label label = getLabelCollector().getLabel(ifStatement.getTarget()).get();
		final BlockStatement boogieThenBlock = makeThenBlock(new GotoStatement(label));
		boogieIfStatement.setCondition(boogieCondition);
		boogieIfStatement.setThen(boogieThenBlock);
		addStatement(boogieIfStatement);
	}

	@Override
	public void caseInvokeStmt(final InvokeStmt invokeStatement) {
		final SootExpression invokeExpression = new SootExpression(invokeStatement.getInvokeExpr());
		invokeExpression.apply(new ProcedureExpressionExtractor(this, null));
	}

	@Override
	public void caseDefault(final Unit unit) {
		throw new IllegalArgumentException("Cannot extract procedure with statement " + unit);
	}

	@Override
	public Optional<Statement> result() {
		return Optional.ofNullable(statement);
	}

}
