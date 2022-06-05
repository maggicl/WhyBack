package byteback.core.converter.soottoboogie.method.procedure;

import byteback.core.converter.soottoboogie.Convention;
import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.converter.soottoboogie.DependencyExtractor;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.expression.ExpressionExtractor;
import byteback.core.converter.soottoboogie.field.FieldConverter;
import byteback.core.converter.soottoboogie.statement.StatementConversionException;
import byteback.core.converter.soottoboogie.type.ReferenceTypeConverter;
import byteback.core.converter.soottoboogie.type.TypeAccessExtractor;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootField;
import byteback.frontend.boogie.ast.Assignee;
import byteback.frontend.boogie.ast.AssignmentStatement;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.GotoStatement;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.ReturnStatement;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.builder.IfStatementBuilder;
import java.util.Optional;
import java.util.function.Supplier;
import soot.IntType;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StaticFieldRef;

public class ProcedureStatementExtractor extends SootStatementVisitor<Body> {

	interface ReferenceSupplier extends Supplier<Optional<ValueReference>> {
	}

	private final ProcedureBodyExtractor bodyExtractor;

	public ProcedureStatementExtractor(final ProcedureBodyExtractor bodyExtractor) {
		this.bodyExtractor = bodyExtractor;
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
		final var left = new SootExpression(assignment.getLeftOp());
		final var right = new SootExpression(assignment.getRightOp());

		left.apply(new SootExpressionVisitor<>() {

			@Override
			public void caseLocal(final Local local) {
				final ValueReference reference = ValueReference.of(ExpressionExtractor.localName(local));
				final ReferenceSupplier referenceSupplier = () -> Optional.of(reference);
				final var extractor = new ProcedureExpressionExtractor(bodyExtractor, referenceSupplier, assignment);
				final Expression assigned = extractor.visit(right, left.getType());

				if (!assigned.equals(reference)) {
					addSingleAssignment(Assignee.of(reference), assigned);
					bodyExtractor.getSubstitutor().put(local, new DependencyExtractor().visit(right), assigned);
				}
			}

			@Override
			public void caseInstanceFieldRef(final InstanceFieldRef instanceFieldReference) {
				final var field = new SootField(instanceFieldReference.getField());
				final var base = new SootExpression(instanceFieldReference.getBase());
				final ReferenceSupplier referenceSupplier = () -> {
					final TypeAccess typeAccess = new TypeAccessExtractor().visit(field.getType());

					return Optional.of(bodyExtractor.getVariableProvider().get(typeAccess));
				};
				final var extractor = new ProcedureExpressionExtractor(bodyExtractor, referenceSupplier, assignment);
				final Expression assigned = extractor.visit(right, left.getType());
				final Expression fieldReference = ValueReference.of(FieldConverter.fieldName(field));
				final Expression boogieBase = new ExpressionExtractor().visit(base);
				addStatement(Prelude.instance().makeHeapUpdateStatement(boogieBase, fieldReference, assigned));
			}

			@Override
			public void caseStaticFieldRef(final StaticFieldRef staticFieldReference) {
				final var field = new SootField(staticFieldReference.getField());
				final ReferenceSupplier referenceSupplier = () -> {
					final TypeAccess typeAccess = new TypeAccessExtractor().visit(field.getType());

					return Optional.of(bodyExtractor.getVariableProvider().get(typeAccess));
				};
				final var extractor = new ProcedureExpressionExtractor(bodyExtractor, referenceSupplier, assignment);
				final Expression assigned = extractor.visit(right, left.getType());
				final Expression fieldReference = ValueReference.of(FieldConverter.fieldName(field));
				final Expression boogieBase = ValueReference.of(ReferenceTypeConverter.typeName(field.getSootClass()));
				addStatement(Prelude.instance().makeStaticUpdateStatement(boogieBase, fieldReference, assigned));
			}

			@Override
			public void caseArrayRef(final ArrayRef arrayReference) {
				final var base = new SootExpression(arrayReference.getBase());
				final var index = new SootExpression(arrayReference.getIndex());
				final var type = new SootType(arrayReference.getType());
				final ReferenceSupplier referenceSupplier = () -> {
					final TypeAccess typeAccess = new TypeAccessExtractor().visit(type);

					return Optional.of(bodyExtractor.getVariableProvider().get(typeAccess));
				};
				final var extractor = new ProcedureExpressionExtractor(bodyExtractor, referenceSupplier, assignment);
				final Expression assigned = extractor.visit(right, left.getType());
				final Expression indexReference = new ProcedureExpressionExtractor(bodyExtractor, assignment)
						.visit(index);
				final Expression boogieBase = new ExpressionExtractor().visit(base);
				addStatement(Prelude.instance().makeArrayUpdateStatement(new TypeAccessExtractor().visit(type),
						boogieBase, indexReference, assigned));
				bodyExtractor.getSubstitutor().prune(arrayReference);
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
		final var operand = new SootExpression(returnStatement.getOp());
		final ValueReference valueReference = Convention.makeReturnReference();
		final var assignee = Assignee.of(valueReference);
		final Expression expression = new ProcedureExpressionExtractor(bodyExtractor, returnStatement).visit(operand,
				bodyExtractor.getReturnType());
		addSingleAssignment(assignee, expression);
		addStatement(new ReturnStatement());
	}

	@Override
	public void caseGotoStmt(final GotoStmt gotoStatement) {
		final Unit targetUnit = gotoStatement.getTarget();
		final Label label = bodyExtractor.getLabelCollector().fetchLabel(targetUnit);
		addStatement(new GotoStatement(label));
	}

	@Override
	public void caseIfStmt(final IfStmt ifStatement) {
		final var type = new SootType(IntType.v());
		final var ifBuilder = new IfStatementBuilder();
		final var condition = new SootExpression(ifStatement.getCondition());
		final Label label = bodyExtractor.getLabelCollector().fetchLabel(ifStatement.getTarget());
		ifBuilder.condition(new ProcedureExpressionExtractor(bodyExtractor, ifStatement).visit(condition, type))
				.thenStatement(new GotoStatement(label));
		addStatement(ifBuilder.build());
	}

	@Override
	public void caseInvokeStmt(final InvokeStmt invokeStatement) {
		final var invoke = new SootExpression(invokeStatement.getInvokeExpr());
		invoke.apply(new ProcedureExpressionExtractor(bodyExtractor, invokeStatement));
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
