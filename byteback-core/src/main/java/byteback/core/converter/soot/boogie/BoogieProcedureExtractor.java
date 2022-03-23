package byteback.core.converter.soot.boogie;

import byteback.core.converter.soot.SootLocalExtractor;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootMethodUnit;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.Assignee;
import byteback.frontend.boogie.ast.AssignmentStatement;
import byteback.frontend.boogie.ast.BlockStatement;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.BoundedBinding;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.GotoStatement;
import byteback.frontend.boogie.ast.IfStatement;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.LabelStatement;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.ProcedureDeclaration;
import byteback.frontend.boogie.ast.ReturnStatement;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.ast.VariableDeclaration;
import byteback.frontend.boogie.builder.BoundedBindingBuilder;
import byteback.frontend.boogie.builder.ProcedureDeclarationBuilder;
import byteback.frontend.boogie.builder.ProcedureSignatureBuilder;
import byteback.frontend.boogie.builder.VariableDeclarationBuilder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import soot.BooleanType;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;

public class BoogieProcedureExtractor extends SootStatementVisitor<ProcedureDeclaration> {

	private final SootMethodUnit methodUnit;

	private final ProcedureDeclarationBuilder procedureBuilder;

	private final ProcedureSignatureBuilder signatureBuilder;

	private final Body body;

	private final Set<Local> initialized = new HashSet<>();

	private final InnerExtractor extractor;

	private int labelCounter;

	private final Map<Unit, Label> labelIndex;

	public BoogieProcedureExtractor(final SootMethodUnit methodUnit, final ProcedureDeclarationBuilder procedureBuilder,
			final ProcedureSignatureBuilder signatureBuilder) {
		this.methodUnit = methodUnit;
		this.procedureBuilder = procedureBuilder;
		this.signatureBuilder = signatureBuilder;
		this.body = new Body();
		this.extractor = new InnerExtractor();
		this.labelCounter = 0;
		this.labelIndex = new HashMap<>();
	}

	private class InnerExtractor extends SootStatementVisitor<ProcedureDeclaration> {

		@Override
		public void caseIdentityStmt(final IdentityStmt identity) {
			final SootExpression left = new SootExpression(identity.getLeftOp());
			final Local local = new SootLocalExtractor().visit(left);
			addParameter(local);
		}

		@Override
		public void caseAssignStmt(final AssignStmt assignment) {
			final SootExpression left = new SootExpression(assignment.getLeftOp());
			final SootExpression right = new SootExpression(assignment.getRightOp());

			left.apply(new SootExpressionVisitor<>() {

				@Override
				public void caseLocal(final Local local) {
					final SootType type = new SootType(local.getType());
					final Assignee assignee = new Assignee();
					final Expression expression = new BoogieExpressionExtractor(type).visit(right);
					assignee.setReference(new ValueReference(new Accessor(local.getName())));

					if (!initialized.contains(local)) {
						addLocal(local);
					}

					addSingleAssignment(assignee, expression);
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
		public void caseReturnStmt(final ReturnStmt returns) {
			final SootExpression operand = new SootExpression(returns.getOp());
			final ValueReference valueReference = BoogiePrelude.getReturnValueReference();
			final Assignee assignee = new Assignee(valueReference);
			final Expression expression = new BoogieExpressionExtractor(methodUnit.getReturnType()).visit(operand);
			addSingleAssignment(assignee, expression);
			addReturnStatement();
		}

		@Override
		public void caseIfStmt(final IfStmt ifStatement) {
			final Unit thenUnit = ifStatement.getTarget();
			final SootExpression condition = new SootExpression(ifStatement.getCondition());
			final Expression boogieCondition = new BoogieExpressionExtractor(new SootType(BooleanType.v()))
					.visit(condition);
			final IfStatement boogieIfStatement = new IfStatement();
			Label label = labelIndex.get(thenUnit);

			if (label == null) {
				label = nextLabel();
			}

			final BlockStatement boogieThenBlock = buildUnitBlock(new GotoStatement(label));

			boogieIfStatement.setCondition(boogieCondition);
			boogieIfStatement.setThen(boogieThenBlock);
		}

		@Override
		public void caseDefault(final Unit unit) {
			throw new UnsupportedOperationException("Cannot collect statements of type " + unit.getClass().getName());
		}

		@Override
		public ProcedureDeclaration result() {
			return procedureBuilder.signature(signatureBuilder.build()).body(body).build();
		}
	}

	public Label nextLabel() {
		final String name = "label" + (labelCounter++);

		return new Label(name);
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

	public BoundedBinding bind(final Local local) {
		final SootType type = new SootType(local.getType());
		final TypeAccess typeAccess = new BoogieTypeAccessExtractor().visit(type);
		final BoundedBinding binding = new BoundedBindingBuilder().addName(local.getName()).typeAccess(typeAccess)
				.build();
		initialized.add(local);

		return binding;
	}

	public void addLocal(final Local local) {
		final BoundedBinding binding = bind(local);
		final VariableDeclaration variableDeclaration = new VariableDeclarationBuilder().addBinding(binding).build();
		body.addLocalDeclaration(variableDeclaration);
	}

	public void addParameter(final Local local) {
		final BoundedBinding binding = bind(local);
		signatureBuilder.addInputBinding(binding);
	}

	@Override
	public void caseDefault(final Unit unit) {
		if (labelIndex.containsKey(unit)) {
			addStatement(new LabelStatement(labelIndex.get(unit)));
		}

		unit.apply(extractor);
	}

	@Override
	public ProcedureDeclaration result() {
		return extractor.result();
	}

}
