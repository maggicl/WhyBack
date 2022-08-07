package byteback.core.converter.soottoboogie.method.procedure;

import byteback.core.converter.soottoboogie.Convention;
import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.expression.ExpressionExtractor;
import byteback.core.converter.soottoboogie.field.FieldConverter;
import byteback.core.converter.soottoboogie.statement.StatementConversionException;
import byteback.core.converter.soottoboogie.type.ReferenceTypeConverter;
import byteback.core.converter.soottoboogie.type.TypeAccessExtractor;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
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
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.builder.IfStatementBuilder;
import java.util.Iterator;
import java.util.function.Supplier;
import soot.IntType;
import soot.Local;
import soot.SootField;
import soot.Type;
import soot.Unit;
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

public class ProcedureStatementExtractor extends SootStatementVisitor<Body> {

	interface ReferenceSupplier extends Supplier<ValueReference> {
	}

	private final ProcedureBodyExtractor bodyExtractor;

	public ProcedureStatementExtractor(final ProcedureBodyExtractor bodyExtractor) {
		this.bodyExtractor = bodyExtractor;
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

		left.apply(new SootExpressionVisitor<>() {

			@Override
			public void caseLocal(final Local local) {
				final ValueReference reference = ValueReference.of(ExpressionExtractor.localName(local));
				final ReferenceSupplier referenceSupplier = () -> reference;
				final var extractor = new ProcedureExpressionExtractor(bodyExtractor, referenceSupplier);
				final Expression assigned = extractor.visit(right, left.getType());

				if (!assigned.equals(reference)) {
					addSingleAssignment(Assignee.of(reference), assigned);
				}
			}

			@Override
			public void caseInstanceFieldRef(final InstanceFieldRef instanceFieldReference) {
				final SootField field = instanceFieldReference.getField();
				final Value base = instanceFieldReference.getBase();
				final ReferenceSupplier referenceSupplier = () -> {
					final TypeAccess typeAccess = new TypeAccessExtractor().visit(field.getType());

					return bodyExtractor.getVariableProvider().get(typeAccess);
				};
				final var extractor = new ProcedureExpressionExtractor(bodyExtractor, referenceSupplier);
				final Expression assigned = extractor.visit(right, left.getType());
				final Expression fieldReference = ValueReference.of(FieldConverter.fieldName(field));
				final Expression boogieBase = new ExpressionExtractor().visit(base);
				addStatement(Prelude.v().makeHeapUpdateStatement(boogieBase, fieldReference, assigned));
			}

			@Override
			public void caseStaticFieldRef(final StaticFieldRef staticFieldReference) {
				final SootField field = staticFieldReference.getField();
				final ReferenceSupplier referenceSupplier = () -> {
					final TypeAccess typeAccess = new TypeAccessExtractor().visit(field.getType());

					return bodyExtractor.getVariableProvider().get(typeAccess);
				};
				final var extractor = new ProcedureExpressionExtractor(bodyExtractor, referenceSupplier);
				final Expression assigned = extractor.visit(right, left.getType());
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
				final ReferenceSupplier referenceSupplier = () -> {
					final TypeAccess typeAccess = new TypeAccessExtractor().visit(type);

					return bodyExtractor.getVariableProvider().get(typeAccess);
				};
				final var extractor = new ProcedureExpressionExtractor(bodyExtractor, referenceSupplier);
				final Expression assigned = extractor.visit(right, left.getType());
				final Expression indexReference = new ProcedureExpressionExtractor(bodyExtractor, getReferenceSupplier(IntType.v())).visit(index);
				final Expression boogieBase = new ExpressionExtractor().visit(base);
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
		final Expression expression = new ProcedureExpressionExtractor(bodyExtractor, getReferenceSupplier(bodyExtractor.getReturnType())).visit(operand,
				bodyExtractor.getReturnType());
		addSingleAssignment(assignee, expression);
		addStatement(new ReturnStatement());
	}

	@Override
	public void caseLookupSwitchStmt(final LookupSwitchStmt switchStatement) {
		final Iterator<Unit> targets = switchStatement.getTargets().iterator();
		final Iterator<IntConstant> values = switchStatement.getLookupValues().iterator();
		final Expression key = new ProcedureExpressionExtractor(bodyExtractor)
				.visit(switchStatement.getKey(), IntType.v());

		while (targets.hasNext() && values.hasNext()) {
			final Unit target = targets.next();
			final Value value = values.next();
			final var ifBuilder = new IfStatementBuilder();
			final Expression index = new ProcedureExpressionExtractor(bodyExtractor).visit(value,
					IntType.v());
			final Label label = bodyExtractor.getLabelCollector().fetchLabel(target);

			ifBuilder.condition(new EqualsOperation(index, key)).thenStatement(new GotoStatement(label));
			addStatement(ifBuilder.build());
		}
	}

	@Override
	public void caseTableSwitchStmt(final TableSwitchStmt switchStatement) {
		final Expression key = new ProcedureExpressionExtractor(bodyExtractor)
				.visit(switchStatement.getKey(), IntType.v());

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
		final Type type = IntType.v();
		final Value condition = ifStatement.getCondition();
		final Label label = bodyExtractor.getLabelCollector().fetchLabel(ifStatement.getTarget());
		ifBuilder.condition(new ProcedureExpressionExtractor(bodyExtractor).visit(condition, type))
				.thenStatement(new GotoStatement(label));
		addStatement(ifBuilder.build());
	}

	@Override
	public void caseInvokeStmt(final InvokeStmt invokeStatement) {
		final var invoke = invokeStatement.getInvokeExpr();
		invoke.apply(new ProcedureExpressionExtractor(bodyExtractor));
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
