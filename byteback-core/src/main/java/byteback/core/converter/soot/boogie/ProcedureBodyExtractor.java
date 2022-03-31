package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootFieldUnit;
import byteback.core.representation.soot.unit.SootMethodUnit;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.AssertStatement;
import byteback.frontend.boogie.ast.Assignee;
import byteback.frontend.boogie.ast.AssumeStatement;
import byteback.frontend.boogie.ast.BlockStatement;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.GotoStatement;
import byteback.frontend.boogie.ast.IfStatement;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.LabelStatement;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.ReturnStatement;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.ValueReference;
import java.util.Map;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;

public class ProcedureBodyExtractor extends SootStatementVisitor<Body> {

	private final Map<Unit, Label> labelTable;

	private final Body body;

	private final SootType returnType;

	private final InnerExtractor extractor;

	private int seed;

	private class InnerExtractor extends SootStatementVisitor<Body> {

		@Override
		public void caseAssignStmt(final AssignStmt assignment) {
			final SootExpression left = new SootExpression(assignment.getLeftOp());
			final SootExpression right = new SootExpression(assignment.getRightOp());
			final Expression boogieRight = new ProcedureExpressionExtractor(left.getType(), body, seed++).visit(right);

			left.apply(new SootExpressionVisitor<>() {

				@Override
				public void caseLocal(final Local local) {
					final ValueReference valueReference = new ValueReference(new Accessor(local.getName()));
					final Assignee assignee = new Assignee(valueReference);
					addSingleAssignment(assignee, boogieRight);
				}

				@Override
				public void caseInstanceFieldRef(final InstanceFieldRef instanceFieldReference) {
					final SootFieldUnit field = new SootFieldUnit(instanceFieldReference.getField());
					final SootExpression base = new SootExpression(instanceFieldReference.getBase());
					final Expression boogieFieldReference = new ValueReference(
							new Accessor(NameConverter.fieldName(field)));
					final Expression boogieBase = new ExpressionExtractor().visit(base);
					addStatement(Prelude.getHeapUpdateStatement(boogieBase, boogieFieldReference, boogieRight));
				}

				@Override
				public void caseDefault(final Value value) {
					throw new IllegalArgumentException(
							"Unknown lhs argument for assignment: " + value.getClass().getName());
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
			final Expression expression = new ExpressionExtractor(returnType).visit(operand);
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
			final Unit targetUnit = ifStatement.getTarget();
			final SootExpression condition = new SootExpression(ifStatement.getCondition());
			final Expression boogieCondition = new IntegerExpressionExtractor().visit(condition);
			final IfStatement boogieIfStatement = new IfStatement();
			final Label label = labelTable.get(targetUnit);
			final BlockStatement boogieThenBlock = makeThenBlock(new GotoStatement(label));
			boogieIfStatement.setCondition(boogieCondition);
			boogieIfStatement.setThen(boogieThenBlock);
			addStatement(boogieIfStatement);
		}

		@Override
		public void caseInvokeStmt(final InvokeStmt invokeStatement) {
			final InvokeExpr invokeExpression = invokeStatement.getInvokeExpr();
			final List<Expression> arguments = new ExpressionExtractor().makeArguments(invokeExpression);
      final SootMethodUnit methodUnit = new SootMethodUnit(invokeExpression.getMethod());

      if (methodUnit.getClassUnit().getName().equals("byteback.annotations.Contract")) {
        addSpecialStatement(methodUnit, arguments);
      } else {
        addStatement(ProcedureExpressionExtractor.makeCall(invokeExpression, arguments));
      }
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

	public ProcedureBodyExtractor(final Body body, final SootType returnType, final Map<Unit, Label> labelTable) {
		this.body = body;
		this.returnType = returnType;
		this.labelTable = labelTable;
		this.seed = 0;
		this.extractor = new InnerExtractor();
	}

  public void addSpecialStatement(final SootMethodUnit methodUnit, final List<Expression> arguments) {
    if (methodUnit.getName().equals("assertion")) {
      assert arguments.getNumChild() == 1;
      addStatement(new AssertStatement(arguments.getChild(0)));
    } else if (methodUnit.getName().equals("assumption")) {
      assert arguments.getNumChild() == 1;
      addStatement(new AssumeStatement(arguments.getChild(0)));
    } else {
      throw new IllegalArgumentException("Invalid special function " + methodUnit);
    }
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
