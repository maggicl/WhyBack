package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootField;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.Assignee;
import byteback.frontend.boogie.ast.BlockStatement;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.GotoStatement;
import byteback.frontend.boogie.ast.IfStatement;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.LabelStatement;
import byteback.frontend.boogie.ast.ReturnStatement;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.ValueReference;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Supplier;

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

public class ProcedureBodyExtractor extends SootStatementVisitor<Body> {

	private final Map<Unit, Label> labelTable;

	private final Body body;

	private final SootType returnType;

	private final InnerExtractor extractor;

  private final Supplier<ValueReference> referenceSupplier;

	public Supplier<ValueReference> makeReferenceSupplier() {
    final AtomicInteger counter = new AtomicInteger();
    counter.set(0);

    return () -> Prelude.generateVariableReference(counter.incrementAndGet());
	}

	private class InnerExtractor extends SootStatementVisitor<Body> {

		@Override
		public void caseAssignStmt(final AssignStmt assignment) {
			final SootExpression left = new SootExpression(assignment.getLeftOp());
			final SootExpression right = new SootExpression(assignment.getRightOp());

			left.apply(new SootExpressionVisitor<>() {

				@Override
				public void caseLocal(final Local local) {
					final ValueReference reference = new ValueReference(new Accessor(local.getName()));
					final Expression assigned = new ProcedureExpressionExtractor(body, reference).visit(right,
							left.getType());

					if (!assigned.equals(reference)) {
						addSingleAssignment(new Assignee(reference), assigned);
					}
				}

				@Override
				public void caseInstanceFieldRef(final InstanceFieldRef instanceFieldReference) {
					final SootField field = new SootField(instanceFieldReference.getField());
					final SootExpression base = new SootExpression(instanceFieldReference.getBase());
          final ValueReference reference = referenceSupplier.get();
					final Expression assigned = new ProcedureExpressionExtractor(body, reference).visit(right,
							left.getType());
					final Expression boogieBase = new ExpressionExtractor().visit(base);
					final Expression boogieFieldReference = new ValueReference(
							new Accessor(NameConverter.fieldName(field)));
					addStatement(Prelude.getHeapUpdateStatement(boogieBase, boogieFieldReference, assigned));
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
			final Label label = labelTable.get(targetUnit);
			addStatement(new GotoStatement(label));
		}

		@Override
		public void caseIfStmt(final IfStmt ifStatement) {
			final SootExpression condition = new SootExpression(ifStatement.getCondition());
			final SootType type = new SootType(IntType.v());
			final Expression boogieCondition = new ExpressionExtractor().visit(condition, type);
			final IfStatement boogieIfStatement = new IfStatement();
			final Label label = labelTable.get(ifStatement.getTarget());
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
		public Body result() {
			return body;
		}

	}

	public ProcedureBodyExtractor(final Body body, final SootType returnType, final Map<Unit, Label> labelTable) {
		this.body = body;
		this.returnType = returnType;
		this.labelTable = labelTable;
		this.extractor = new InnerExtractor();
    this.referenceSupplier = makeReferenceSupplier();
	}

	public BlockStatement makeThenBlock(final Statement statement) {
		final BlockStatement blockStatement = new BlockStatement();
		blockStatement.addStatement(statement);

		return blockStatement;
	}

	public void addStatement(final Statement statement) {
		body.addStatement(statement);
	}

	public void addSingleAssignment(final Assignee assignee, final Expression expression) {
		addStatement(Prelude.makeSingleAssignment(assignee, expression));
	}

	@Override
	public void caseDefault(final Unit unit) {
		if (labelTable.containsKey(unit)) {
			addStatement(new LabelStatement(labelTable.get(unit)));
		}

		unit.apply(extractor);
	}

	@Override
	public Body result() {
		return extractor.result();
	}

}
