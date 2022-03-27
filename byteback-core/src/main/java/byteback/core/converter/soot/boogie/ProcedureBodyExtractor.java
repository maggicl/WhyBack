package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.annotation.SootAnnotation;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.core.representation.soot.unit.SootMethodUnit;
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
import byteback.frontend.boogie.ast.TargetedCallStatement;
import byteback.frontend.boogie.ast.ValueReference;
import java.util.Map;
import java.util.Optional;
import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StaticInvokeExpr;

public class ProcedureBodyExtractor extends SootStatementVisitor<Body> {

	private final Map<Unit, Label> labelIndex;

	private final Body body;

	private final SootType returnType;

	private final InnerExtractor extractor;

	private class IntegerExpressionExtractor extends ExpressionExtractor {

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

			left.apply(new SootExpressionVisitor<>() {

				@Override
				public void caseLocal(final Local local) {
					final SootType type = new SootType(local.getType());
					final ValueReference reference = new ValueReference(new Accessor(local.getName()));
					final ExpressionExtractor extractor = new ExpressionExtractor(type);

					right.apply(new SootExpressionVisitor<>() {

						@Override
						public void caseStaticInvokeExpr(final StaticInvokeExpr invocation) {
							final SootMethodUnit target = new SootMethodUnit(invocation.getMethod());
							final Optional<SootAnnotation> pureAnnotation = target
									.getAnnotation("Lbyteback/annotations/Contract$Pure;");

							if (pureAnnotation.isPresent()) {
								extractor.caseStaticInvokeExpr(invocation);
								addSingleAssignment(new Assignee(reference), extractor.result());
							} else {
								final TargetedCallStatement callStatement = new TargetedCallStatement();
								callStatement.setAccessor(new Accessor(NameConverter.methodName(target)));

								for (Value argument : invocation.getArgs()) {
									final SootExpression expression = new SootExpression(argument);
									final SootType type = new SootType(argument.getType());
									callStatement.addArgument(extractor.argumentExtractor(type).visit(expression));
								}

								callStatement.addTarget(reference);
								addStatement(callStatement);
							}
						}

						@Override
						public void caseDefault(final Value value) {
							addSingleAssignment(new Assignee(reference), extractor.visit(new SootExpression(value)));
						}

					});

				}

				@Override
				public void caseDefault(final Value expression) {
					throw new IllegalArgumentException("Conversion for assignee of type "
							+ expression.getClass().getName() + " is currently not supported");
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
