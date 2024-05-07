package byteback.whyml.vimp.expr;

import byteback.analysis.JimpleStmtSwitch;
import byteback.analysis.JimpleValueSwitch;
import byteback.analysis.Vimp;
import byteback.analysis.tags.PositionTag;
import byteback.analysis.vimp.AssertionStmt;
import byteback.analysis.vimp.AssumptionStmt;
import byteback.analysis.vimp.InvariantStmt;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.statement.CFGLogicalStatement;
import byteback.whyml.syntax.statement.CFGStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import soot.Local;
import soot.SootField;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeStmt;
import soot.jimple.ParameterRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.tagkit.AbstractHost;
import soot.toolkits.graph.Block;

public class ProgramStatementExtractor extends JimpleStmtSwitch<List<CFGStatement>> {
	private final ProcedureExpressionExtractor procedureExpressionExtractor;

	private final List<CFGStatement> statements = new ArrayList<>();

	public ProgramStatementExtractor(ProcedureExpressionExtractor procedureExpressionExtractor) {
		this.procedureExpressionExtractor = procedureExpressionExtractor;
	}

	@Override
	public List<CFGStatement> getResult() {
		return statements;
	}

	private void addStatement(CFGStatement statement) {
		statements.add(statement);
	}

	public List<CFGStatement> visit(final Block block) {
		for (final Unit u : block) {
			u.apply(this);
		}

		return getResult();
	}

	@Override
	public void caseIdentityStmt(final IdentityStmt identity) {
		final Value assignee = identity.getLeftOp();
		final Value assigned = identity.getRightOp();

		if (assignee instanceof Local local) {
			if (assigned instanceof CaughtExceptionRef) {
				visit(Grimp.v().newAssignStmt(local, Vimp.v().newCaughtExceptionRef()));
			}

			if (assigned instanceof ParameterRef || assigned instanceof ThisRef) {
				final var variableBuilder = new VariableDeclarationBuilder();
				final BoundedBinding variableBinding = ProcedureConverter.makeBinding(local);
				bodyExtractor.addLocalDeclaration(variableBuilder.addBinding(variableBinding).build());

				final var assignment = new AssignmentStatement(
						Assignee.of(ValueReference.of(PureExpressionExtractor.localName(local))),
						ValueReference.of(ProcedureConverter.parameterName(local)));
				addStatement(assignment);
			}
		}
	}

	@Override
	public void caseAssignStmt(final AssignStmt assignment) {
		final Value left = assignment.getLeftOp();
		final Value right = assignment.getRightOp();

		left.apply(new JimpleValueSwitch<>() {

			@Override
			public void caseLocal(final Local local) {
				final ValueReference reference = ValueReference.of(PureExpressionExtractor.localName(local));
				final AtomicBoolean referenceWasAssigned = new AtomicBoolean();
				final Expression assigned = makeExpressionExtractor(expectedType -> {
					referenceWasAssigned.set(true);
					return reference;
				}).visit(right);

				if (!referenceWasAssigned.get()) {
					addSingleAssignment(Assignee.of(reference), assigned);
				}
			}

			@Override
			public void caseInstanceFieldRef(final InstanceFieldRef instanceFieldReference) {
				final SootField field = instanceFieldReference.getField();
				final Value base = instanceFieldReference.getBase();
				final Expression assigned = makeExpressionExtractor().visit(right);
				final Expression fieldReference = ValueReference.of(FieldConverter.fieldName(field));
				final Expression boogieBase = new PureExpressionExtractor().visit(base);
				addStatement(Prelude.v().makeHeapUpdateStatement(boogieBase, fieldReference, assigned));
			}

			@Override
			public void caseStaticFieldRef(final StaticFieldRef staticFieldReference) {
				final SootField field = staticFieldReference.getField();
				final Expression assigned = new ProgramExpressionExtractor(bodyExtractor).visit(right);
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
				final Expression assigned = makeExpressionExtractor().visit(right);
				final Expression indexReference = makeExpressionExtractor().visit(index);
				final Expression boogieBase = new PureExpressionExtractor().visit(base);
				addStatement(Prelude.v().makeArrayUpdateStatement(new TypeAccessExtractor().visit(type), boogieBase,
						indexReference, assigned));
			}

			@Override
			public void caseCaughtExceptionRef(final CaughtExceptionRef exceptionReference) {
				final ValueReference reference = Convention.makeExceptionReference();
				final Expression assigned = makeExpressionExtractor().visit(right);
				addSingleAssignment(Assignee.of(reference), assigned);
			}

			@Override
			public void caseDefault(final Value value) {
				throw new StatementConversionException(assignment,
						"Unknown left hand side argument in assignment: " + assignment);
			}

		});
	}

	@Override
	public void caseInvokeStmt(final InvokeStmt invokeStatement) {
		final var invoke = invokeStatement.getInvokeExpr();
		makeExpressionExtractor().visit(invoke);
	}

	private Optional<String> positionAttribute(AbstractHost stmt, String string) {
		if (stmt.hasTag("PositionTag")) {
			final PositionTag tag = (PositionTag) stmt.getTag("PositionTag");
			return Optional.of("%s: (line %d): %s might not hold".formatted(tag.file, tag.lineNumber, string));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public void caseAssertionStmt(final AssertionStmt assertionStmt) {
		final Expression condition = procedureExpressionExtractor.visit(assertionStmt.getCondition());
		final Optional<String> position = positionAttribute(assertionStmt, "assertion");

		addStatement(new CFGLogicalStatement(CFGLogicalStatement.Kind.ASSERT, position, condition));
	}

	@Override
	public void caseAssumptionStmt(final AssumptionStmt assumptionStmt) {
		final Expression condition = procedureExpressionExtractor.visit(assumptionStmt.getCondition());
		final Optional<String> position = positionAttribute(assumptionStmt, "assumption");

		addStatement(new CFGLogicalStatement(CFGLogicalStatement.Kind.ASSUME, position, condition));
	}

	@Override
	public void caseInvariantStmt(final InvariantStmt assumptionStmt) {
		final Expression condition = procedureExpressionExtractor.visit(assumptionStmt.getCondition());
		final Optional<String> position = positionAttribute(assumptionStmt, "invariant");

		addStatement(new CFGLogicalStatement(CFGLogicalStatement.Kind.INVARIANT, position, condition));
	}

	@Override
	public void caseDefault(final Unit unit) {
		throw new IllegalStateException("Cannot extract statements of type " + unit.getClass().getName());
	}

	@Override
	public void caseThrowStmt(final ThrowStmt throwStatement) {
		final Value operand = throwStatement.getOp();

		// assign to caughtexception
		// return

		if (!(operand instanceof CaughtExceptionRef)) {
			final ValueReference valueReference = Convention.makeExceptionReference();
			final var assignee = Assignee.of(valueReference);
			final Expression expression = makeExpressionExtractor().visit(operand);
			addSingleAssignment(assignee, expression);
		}

		addStatement(new ReturnStatement());
	}

	@Override
	public Body result() {
		return bodyExtractor.result();
	}
}
