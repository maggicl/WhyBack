package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.Assignee;
import byteback.frontend.boogie.ast.AssignmentStatement;
import byteback.frontend.boogie.ast.BlockStatement;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.GotoStatement;
import byteback.frontend.boogie.ast.IfStatement;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.LabelStatement;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.ReturnStatement;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.ValueReference;
import java.util.Map;
import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;

public class ProcedureBodyExtractor extends SootStatementVisitor<Body> {

	private final Map<Unit, Label> labelIndex;

	private final Body body;

	private final SootType returnType;

	private final InnerExtractor extractor;

	private int seed;

	private static class IntegerExpressionExtractor extends ExpressionExtractor {

		public IntegerExpressionExtractor() {
			super(new SootType(IntType.v()));
		}

		@Override
		public void caseLocal(final Local local) {
			super.caseLocal(local);
			final SootType type = new SootType(local.getType());

			type.apply(new SootTypeVisitor<>() {

				@Override
				public void caseBooleanType(final BooleanType booleanType) {
					final FunctionReference caster = Prelude.getIntCaster();
					caster.addArgument(operands.pop());
					pushExpression(caster);
				}

				@Override
				public void caseDefault(final Type type) {
					// No need to convert this local.
				}

			});

		}

		@Override
		public ExpressionExtractor argumentExtractor(final SootType type) {
			return new IntegerExpressionExtractor();
		}

	}

	private class InnerExtractor extends SootStatementVisitor<Body> {

		@Override
		public void caseAssignStmt(final AssignStmt assignment) {
			final SootExpression left = new SootExpression(assignment.getLeftOp());
			final SootExpression right = new SootExpression(assignment.getRightOp());
			final Expression rightExpression = new ProcedureExpressionExtractor(left.getType(), body, seed++).visit(right);

			left.apply(new SootExpressionVisitor<>() {

				@Override
				public void caseLocal(final Local local) {
					final ValueReference valueReference = new ValueReference(new Accessor(local.getName()));
					final Assignee assignee = new Assignee(valueReference);
					addSingleAssignment(assignee, rightExpression);
				}

				@Override
				public void caseDefault(final Value value) {
					throw new IllegalArgumentException(
							"Unknown lhs argument for assignment: " + value.getClass().getName());
				}

			});
		}

		@Override
		public void caseReturnVoidStmt(final ReturnVoidStmt returns) {
			addReturnStatement();
		}

		@Override
		public void caseReturnStmt(final ReturnStmt returnStatement) {
			final SootExpression operand = new SootExpression(returnStatement.getOp());
			final ValueReference valueReference = Prelude.getReturnValueReference();
			final Assignee assignee = new Assignee(valueReference);
			final Expression expression = new ExpressionExtractor(returnType).visit(operand);
			addSingleAssignment(assignee, expression);
			addReturnStatement();
		}

		@Override
		public void caseGotoStmt(final GotoStmt gotoStatement) {
			final Unit targetUnit = gotoStatement.getTarget();
			final Label label = labelIndex.get(targetUnit);
			addStatement(new GotoStatement(label));
		}

		@Override
		public void caseIfStmt(final IfStmt ifStatement) {
			final Unit thenUnit = ifStatement.getTarget();
			final SootExpression condition = new SootExpression(ifStatement.getCondition());
			final Expression boogieCondition = new IntegerExpressionExtractor().visit(condition);
			final IfStatement boogieIfStatement = new IfStatement();
			final Label label = labelIndex.get(thenUnit);
			final BlockStatement boogieThenBlock = buildUnitBlock(new GotoStatement(label));
			boogieIfStatement.setCondition(boogieCondition);
			boogieIfStatement.setThen(boogieThenBlock);
			addStatement(boogieIfStatement);
		}

    @Override
    public void caseInvokeStmt(final InvokeStmt invokeStatement) {
      // addStatement(ProcedureExpressionExtractor.makeCallStatement(invokeStatement.getInvokeExpr()));
    }

		@Override
		public void caseDefault(final Unit unit) {
			throw new IllegalArgumentException("Cannot extract statement of type " + unit.getClass().getName());
		}

		@Override
		public Body result() {
			return body;
		}

	}

	public ProcedureBodyExtractor(final Body body, final SootType returnType, final Map<Unit, Label> labelIndex) {
		this.body = body;
		this.seed = 0;
		this.returnType = returnType;
		this.labelIndex = labelIndex;
		this.extractor = new InnerExtractor();
	}

	public BlockStatement buildUnitBlock(final Statement statement) {
		final BlockStatement blockStatement = new BlockStatement();
		blockStatement.addStatement(statement);

		return blockStatement;
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

	@Override
	public void caseDefault(final Unit unit) {
		if (labelIndex.containsKey(unit)) {
			addStatement(new LabelStatement(labelIndex.get(unit)));
		}

		unit.apply(extractor);
	}

	@Override
	public Body result() {
		return extractor.result();
	}

}
