package byteback.core.converter.soottoboogie.method.procedure;

import byteback.core.converter.soottoboogie.Convention;
import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.converter.soottoboogie.method.procedure.LoopCollector.LoopContext;
import byteback.core.converter.soottoboogie.type.TypeAccessExtractor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.LabelStatement;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.ast.VariableDeclaration;
import java.util.Stack;
import soot.Type;
import soot.Unit;
import soot.jimple.IfStmt;

public class ProcedureBodyExtractor extends SootStatementVisitor<Body> {

	public class VariableProvider {

		private int variableCounter;

		public VariableProvider() {
			variableCounter = 0;
		}

		public ValueReference get(final TypeAccess typeAccess) {
			final ValueReference reference = Convention.makeValueReference(++variableCounter);
			final VariableDeclaration declaration = reference.makeVariableDeclaration(typeAccess);
			body.addLocalDeclaration(declaration);

			return reference;
		}

	}

	private final Type returnType;

	private final Body body;

	private final VariableProvider variableProvider;

	private final LabelCollector labelCollector;

	private final LoopCollector loopCollector;

	private final Stack<LoopContext> activeLoops;

	public ProcedureBodyExtractor(final Type returnType) {
		this.returnType = returnType;
		this.body = new Body();
		this.variableProvider = new VariableProvider();
		this.labelCollector = new LabelCollector();
		this.loopCollector = new LoopCollector();
		this.activeLoops = new Stack<>();
	}

	public Body visit(final soot.Body body) {
		labelCollector.collect(body);
		loopCollector.collect(body);

		return super.visit(body);
	}

	public void addStatement(final Statement statement) {
		body.addStatement(statement);
	}

	public void addInvariant(final Expression argument) {
		if (activeLoops.isEmpty()) {
			throw new ConversionException("Trying to insert an invariant outside of a loop context");
		}

		activeLoops.peek().addInvariant(argument);
	}

	public Body getBody() {
		return body;
	}

	public Type getReturnType() {
		return returnType;
	}

	public LoopCollector getLoopCollector() {
		return loopCollector;
	}

	public LabelCollector getLabelCollector() {
		return labelCollector;
	}

	public VariableProvider getVariableProvider() {
		return variableProvider;
	}

	public ValueReference generateReference(final Type type) {
		final TypeAccess access = new TypeAccessExtractor().visit(type);

		return variableProvider.get(access);
	}

	@Override
	public void caseIfStmt(final IfStmt ifStatement) {
		caseDefault(ifStatement);
	}

	@Override
	public void caseDefault(final Unit unit) {
		final var extractor = new ProcedureStatementExtractor(this);
		loopCollector.getByHead(unit).ifPresent((context) -> {
			activeLoops.push(context);
			addStatement(context.getAssertionPoint());
		});
		loopCollector.getByBackJump(unit).ifPresent((context) -> {
			assert context.getLoop() == activeLoops.peek().getLoop();
			addStatement(activeLoops.pop().getAssertionPoint());
		});
		labelCollector.getLabel(unit).ifPresent((label) -> {
			body.addStatement(new LabelStatement(label));
		});
		loopCollector.getByExitTarget(unit).ifPresent((context) -> {
			addStatement(context.getAssumptionPoint());
		});
		loopCollector.getByExit(unit).ifPresent((context) -> {
			addStatement(context.getAssumptionPoint());
		});
		extractor.visit(unit);
	}

	@Override
	public Body result() {
		return body;
	}

}
