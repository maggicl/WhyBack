package byteback.converter.soottoboogie.method.procedure;

import byteback.analysis.JimpleStmtSwitch;
import byteback.analysis.JimpleValueSwitch;
import byteback.converter.soottoboogie.Convention;
import byteback.converter.soottoboogie.ConversionException;
import byteback.converter.soottoboogie.Prelude;
import byteback.converter.soottoboogie.expression.ExpressionExtractor;
import byteback.converter.soottoboogie.field.FieldConverter;
import byteback.converter.soottoboogie.statement.StatementConversionException;
import byteback.converter.soottoboogie.type.ReferenceTypeConverter;
import byteback.converter.soottoboogie.type.TypeAccessExtractor;
import byteback.frontend.boogie.ast.Assignee;
import byteback.frontend.boogie.ast.AssignmentStatement;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.EqualsOperation;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.GotoStatement;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.NumberLiteral;
import byteback.frontend.boogie.ast.ReturnStatement;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.builder.IfStatementBuilder;
import java.util.Iterator;
import java.util.function.Supplier;
import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.SootField;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.TableSwitchStmt;

public class ProcedureStatementExtractor extends JimpleStmtSwitch<Body> {

	interface ReferenceSupplier extends Supplier<ValueReference> {
	}

	private final ProcedureBodyExtractor bodyExtractor;

	public ProcedureStatementExtractor(final ProcedureBodyExtractor bodyExtractor) {
		this.bodyExtractor = bodyExtractor;
	}

	public ProcedureExpressionExtractor makeExpressionExtractor(final Type type) {
		return new ProcedureExpressionExtractor(type, bodyExtractor);
	}

	public ReferenceSupplier getReferenceSupplier(final Type type) {
		return () -> bodyExtractor.generateReference(type);
	}

	public void addStatement(final Statement statement) {
		bodyExtractor.addStatement(statement);
	}

	public void addSingleAssignment(final Assignee assignee, final Expression expression) {
		addStatement(new AssignmentStatement(assignee, expression));
	}

	public Body visit(final Unit unit) {
		try {
			unit.apply(this);

			return result();
		} catch (final ConversionException exception) {
			throw new StatementConversionException(unit, exception);
		}
	}

	@Override
	public void caseAssignStmt(final AssignStmt assignment) {
		final Value left = assignment.getLeftOp();
		final Value right = assignment.getRightOp();

		left.apply(new JimpleValueSwitch<>() {

			@Override
			public void caseLocal(final Local local) {
				final ValueReference reference = ValueReference.of(ExpressionExtractor.localName(local));
				final Expression assigned = makeExpressionExtractor(left.getType()).visit(right);

				addSingleAssignment(Assignee.of(reference), assigned);
			}

			@Override
			public void caseInstanceFieldRef(final InstanceFieldRef instanceFieldReference) {
				final SootField field = instanceFieldReference.getField();
				final Value base = instanceFieldReference.getBase();
				final Expression assigned = makeExpressionExtractor(left.getType()).visit(right);
				final Expression fieldReference = ValueReference.of(FieldConverter.fieldName(field));
				final Expression boogieBase = new ExpressionExtractor(UnknownType.v()).visit(base);
				addStatement(Prelude.v().makeHeapUpdateStatement(boogieBase, fieldReference, assigned));
			}

			@Override
			public void caseStaticFieldRef(final StaticFieldRef staticFieldReference) {
				final SootField field = staticFieldReference.getField();
				final Expression assigned = new ProcedureExpressionExtractor(left.getType(), bodyExtractor)
						.visit(right);
				final Expression fieldReference = ValueReference.of(FieldConverter.fieldName(field));
				final Expression boogieBase = ValueReference
						.of(ReferenceTypeConverter.typeName(field.getDeclaringClass()));
				addStatement(Prelude.v().makeStaticUpdateStatement(boogieBase, fieldReference, assigned));
			}

			@Override
			public void caseArrayRef(final ArrayRef arrayReference) {
				final Value base = arrayReference.getBase();
				final Value index = arrayReference.getIndex();
				final Type type = arrayReference.getType();
				final Expression assigned = makeExpressionExtractor(left.getType()).visit(right);
				final Expression indexReference = makeExpressionExtractor(IntType.v()).visit(index);
				final Expression boogieBase = new ExpressionExtractor(UnknownType.v()).visit(base);
				addStatement(Prelude.v().makeArrayUpdateStatement(new TypeAccessExtractor().visit(type), boogieBase,
						indexReference, assigned));
			}

			@Override
			public void caseDefault(final Value value) {
				throw new StatementConversionException(assignment,
						"Unknown left hand side argument in assignment: " + assignment);
			}

		});
	}

	@Override
	public void caseReturnVoidStmt(final ReturnVoidStmt returnStatement) {
		addStatement(new ReturnStatement());
	}

	@Override
	public void caseReturnStmt(final ReturnStmt returnStatement) {
		final Value operand = returnStatement.getOp();
		final ValueReference valueReference = Convention.makeReturnReference();
		final var assignee = Assignee.of(valueReference);
		final Expression expression = makeExpressionExtractor(bodyExtractor.getReturnType()).visit(operand);
		addSingleAssignment(assignee, expression);
		addStatement(new ReturnStatement());
	}

	@Override
	public void caseLookupSwitchStmt(final LookupSwitchStmt switchStatement) {
		final Iterator<Unit> targets = switchStatement.getTargets().iterator();
		final Iterator<IntConstant> values = switchStatement.getLookupValues().iterator();
		final Expression key = new ProcedureExpressionExtractor(IntType.v(), bodyExtractor)
				.visit(switchStatement.getKey());

		while (targets.hasNext() && values.hasNext()) {
			final Unit target = targets.next();
			final Value value = values.next();
			final var ifBuilder = new IfStatementBuilder();
			final Expression index = new ProcedureExpressionExtractor(IntType.v(), bodyExtractor).visit(value);
			final Label label = bodyExtractor.getLabelCollector().fetchLabel(target);

			ifBuilder.condition(new EqualsOperation(index, key)).thenStatement(new GotoStatement(label));
			addStatement(ifBuilder.build());
		}
	}

	@Override
	public void caseTableSwitchStmt(final TableSwitchStmt switchStatement) {
		final Expression key = new ProcedureExpressionExtractor(IntType.v(), bodyExtractor)
				.visit(switchStatement.getKey());

		for (int i = switchStatement.getLowIndex(); i < switchStatement.getHighIndex(); ++i) {
			final Unit target = switchStatement.getTarget(i);
			final var ifBuilder = new IfStatementBuilder();
			final Expression index = new NumberLiteral(Integer.toString(i));
			final Label label = bodyExtractor.getLabelCollector().fetchLabel(target);

			ifBuilder.condition(new EqualsOperation(index, key)).thenStatement(new GotoStatement(label));
			addStatement(ifBuilder.build());
		}
	}

	@Override
	public void caseGotoStmt(final GotoStmt gotoStatement) {
		final Unit targetUnit = gotoStatement.getTarget();
		final Label label = bodyExtractor.getLabelCollector().fetchLabel(targetUnit);
		addStatement(new GotoStatement(label));
	}

	@Override
	public void caseIfStmt(final IfStmt ifStatement) {
		final var ifBuilder = new IfStatementBuilder();
		final Type type = BooleanType.v();
		final Value condition = ifStatement.getCondition();
		final Label label = bodyExtractor.getLabelCollector().fetchLabel(ifStatement.getTarget());
		ifBuilder.condition(new ProcedureExpressionExtractor(type, bodyExtractor).visit(condition))
				.thenStatement(new GotoStatement(label));
		addStatement(ifBuilder.build());
	}

	@Override
	public void caseInvokeStmt(final InvokeStmt invokeStatement) {
		final var invoke = invokeStatement.getInvokeExpr();
		makeExpressionExtractor(invoke.getType()).visit(invoke);
	}

	@Override
	public void caseDefault(final Unit unit) {
		throw new StatementConversionException(unit, "Cannot extract statements of type " + unit.getClass().getName());
	}

	@Override
	public Body result() {
		return bodyExtractor.result();
	}

}
